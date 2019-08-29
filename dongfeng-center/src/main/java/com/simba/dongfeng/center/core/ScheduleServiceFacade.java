package com.simba.dongfeng.center.core;

import com.simba.dongfeng.center.core.route.ExecutorRouterStg;
import com.simba.dongfeng.center.dao.*;
import com.simba.dongfeng.center.enums.DagStatusEnum;
import com.simba.dongfeng.center.enums.ExecutorRouterStgEnum;
import com.simba.dongfeng.center.pojo.*;
import com.simba.dongfeng.common.enums.JobStatusEnum;
import com.simba.dongfeng.common.pojo.JobInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * DATE:   2019-08-15 17:04
 * AUTHOR: simba.hjf
 * DESC:    调度中心进行调度所需的服务方法门面
 **/
@Service
public class ScheduleServiceFacade{
    private Logger logger = LoggerFactory.getLogger(ScheduleServiceFacade.class);

    //注入self,为了自身方法事务
    @Resource
    private ScheduleServiceFacade self;

    @Resource
    private DagDao dagDao;
    @Resource
    private JobDao jobDao;
    @Resource
    private ExecutorDao executorDao;
    @Resource
    private DependencyDao dependencyDao;
    @Resource
    private DagTriggerLogDao dagTriggerLogDao;
    @Resource
    private JobTriggerLogDao jobTriggerLogDao;


    /**
     * 拉取60秒时间窗口内需要被调度的dag,更新下次调度时间
     * 事务只是为了操作期间锁表
     *
     * @return
     */
    @Transactional(value = "transactionManager")
    public List<DagDto> fetchNeedTriggerDag(int fetchTimeWindow) {
        Timestamp endTimeline = Timestamp.valueOf(LocalDateTime.now().plusSeconds(fetchTimeWindow));
        Timestamp nowTimeline = Timestamp.valueOf(LocalDateTime.now());
        List<DagDto> dagDtoList = Optional.ofNullable(dagDao.selectNeedTriggerDagWithLock(endTimeline)).orElse(new ArrayList<>());
        Iterator<DagDto> iterator = dagDtoList.iterator();
        while (iterator.hasNext()) {
            DagDto dagDto = iterator.next();
            try {
                System.out.println(dagDto);
                if (dagDto.getTriggerTime() != null && dagDto.getTriggerTime().before(nowTimeline)) {
                    iterator.remove();
                }
                CronExpression cronExpression = new CronExpression(dagDto.getDagCron());
                Date nextValidTime = cronExpression.getNextValidTimeAfter(endTimeline);
                dagDao.updateDagTriggerTime(dagDto.getId(), cronExpression.getNextValidTimeAfter(endTimeline));
                System.out.println(nextValidTime);
            } catch (ParseException e) {
                logger.error("parse dag trigger cron err", e);
                //TODO:  ALARM,  DO NOT THROW EXCEPTION
            }
        }
        return dagDtoList;
    }

    public int insertOrUploadJobTriggerLogWhenTriggerNewJob(JobTriggerLogDto jobTriggerLog) {
        JobTriggerLogDto jobLog = jobTriggerLogDao.selectJobTriggerLogDtoByJobAndDag(jobTriggerLog.getJobId(),jobTriggerLog.getDagTriggerId(), false);
        if (jobLog == null) {
            return jobTriggerLogDao.inserJobTriggerLog(jobTriggerLog);
        } else {
            if (jobLog.getStatus() != JobStatusEnum.INITIAL.getValue()) {
                logger.info("jobTriggerLog has been handled.jobTriggerLog:" + jobTriggerLog);
                return 0;
            }
            return jobTriggerLogDao.updateJobTriggerLogWithAssignedCenter(jobTriggerLog);
        }
    }

    /**
     * 选取执行器
     *
     * @param jobDto
     * @return
     */
    public ExecutorDto route(JobDto jobDto) {
        DagDto dagDto = dagDao.selectDagById(jobDto.getDagId());
        List<ExecutorDto> executorDtoList = Optional.ofNullable(executorDao.selectExecutorsByGroup(dagDto.getDagGroup())).orElse(new ArrayList<>());
        ExecutorRouterStg executorRouterStg = ExecutorRouterStgEnum.getExecutorRouterStgEnum(jobDto.getScheduleType()).getExecutorRouterStg();
        ExecutorDto executor = executorRouterStg.route(jobDto, executorDtoList);
        return executor;
    }

    /**
     * 任务分发到执行器
     *
     * @param jobInfo
     * @param executorDto
     */
    public void dispatch(JobInfo jobInfo, ExecutorDto executorDto) {

        throw new RuntimeException("dispatch err.jobInfo:" + jobInfo + ",executorDto:" + executorDto);
    }


    /**
     * 写调度日志,调度job(TASK_NODE节点)
     *
     * @param jobDto
     * @param dagTriggerLogDto
     * @param jobScheduleRetryTime
     */
    public void scheduleJob(JobDto jobDto, DagTriggerLogDto dagTriggerLogDto, int jobScheduleRetryTime,String centerIp) {
        int scheduleCnt = 0;
        ExecutorDto executor = null;
        JobTriggerLogDto curChildJobTriggerLog = generateJobTriggerLogDto(jobDto.getId(), dagTriggerLogDto.getDagId(), dagTriggerLogDto.getId(), JobStatusEnum.INITIAL.getValue(), centerIp,null, dagTriggerLogDto.getParam());
        while (true) {
            try {
                /**
                 * 先写调度日志,再调度
                 */
                executor = this.route(jobDto);
                curChildJobTriggerLog.setExecutorIp(executor.getExecutorIp());
                int rs = self.insertOrUploadJobTriggerLogWhenTriggerNewJob(curChildJobTriggerLog);
                if (rs == 1) {
                    JobInfo jobInfo = this.generateJobInfo(jobDto, curChildJobTriggerLog);
                    this.dispatch(jobInfo, executor);
                    curChildJobTriggerLog.setStatus(JobStatusEnum.RUNNING.getValue());
                    jobTriggerLogDao.updateJobTriggerLog(curChildJobTriggerLog);
                }
                break;
            } catch (Exception e) {
                if (scheduleCnt++ < jobScheduleRetryTime) {
                    logger.info("job schedule retry:" + scheduleCnt + ",job:" + jobDto.toString() + ",executor:" + executor.toString());
                    continue;
                } else {
                    JobTriggerLogDto jobTriggerLog = this.selectJobTriggerLogDtoByJobAndDag(curChildJobTriggerLog.getJobId(), curChildJobTriggerLog.getDagTriggerId(), false);
                    if (jobTriggerLog.getStatus() == JobStatusEnum.INITIAL.getValue() && jobTriggerLog.getCenterIp().equals(centerIp)) {
                        curChildJobTriggerLog.setStatus(JobStatusEnum.FAIL.getValue());
                        curChildJobTriggerLog.setEndTime(new Date());
                        jobTriggerLogDao.updateJobTriggerLogWithAssignedCenter(curChildJobTriggerLog);

                        dagTriggerLogDto.setStatus(DagStatusEnum.FAIL.getValue());
                        dagTriggerLogDto.setEndTime(new Date());
                        dagTriggerLogDao.updateDagTriggerLog(dagTriggerLogDto);

                        //TODO alarm
                        logger.error("job schedule failed.job:" + jobDto);
                        break;
                    }
                }
            }
        }
    }


    /**
     * 根据dagId获取dag
     *
     * @param dagId
     */
    public DagDto selectDagById(long dagId) {
        return dagDao.selectDagById(dagId);
    }


    /**
     * 获取dag流的开始任务
     *
     * @param dagDto
     * @return
     */
    public JobDto selectStartJobWithDagId(DagDto dagDto) {
        return jobDao.selectStartJobByDagId(dagDto.getId());
    }

    /**
     * 获取子job列表
     *
     * @param jobId
     * @return
     */
    public List<JobDto> selectChildJobList(long jobId) {
        List<Long> childJobIdList = dependencyDao.selectChildJobIdList(jobId);
        List<JobDto> jobDtoList = jobDao.selectJobDtoListByJobIds(childJobIdList);
        return jobDtoList;
    }

    /**
     * 获取父job列表
     *
     * @param jobId
     * @return
     */
    public List<JobDto> selectParentJobList(long jobId) {
        List<Long> childJobIdList = dependencyDao.selectParentJobIdList(jobId);
        List<JobDto> jobDtoList = jobDao.selectJobDtoListByJobIds(childJobIdList);
        return jobDtoList;
    }


    /**
     * 根据dagTriggerId获取DagTriggerLogDto
     *
     * @param dagTriggerId
     * @return
     */
    public DagTriggerLogDto selectDagTriggerLogById(long dagTriggerId) {
        return dagTriggerLogDao.selectDagTriggerLogById(dagTriggerId);
    }


    /**
     * 写入dag触发日志
     *
     * @param dagTriggerLogDto
     * @return
     */
    public int insertDagTriggerLog(DagTriggerLogDto dagTriggerLogDto) {
        return dagTriggerLogDao.insertDagTriggerLog(dagTriggerLogDto);
    }

    /**
     * 更新dagTriggerLog
     *
     * @param dagTriggerLogDto
     * @return
     */
    public int updateDagTriggerLog(DagTriggerLogDto dagTriggerLogDto) {
        return dagTriggerLogDao.updateDagTriggerLog(dagTriggerLogDto);
    }

    /**
     * 写入job触发日志
     *
     * @param jobTriggerLogDto
     * @return
     */
    public void insertJobTriggerLog(JobTriggerLogDto jobTriggerLogDto) {
        jobTriggerLogDao.inserJobTriggerLog(jobTriggerLogDto);
    }

    /**
     * 根据id查询JobTriggerLog
     *
     * @param id
     * @return
     */
    public JobTriggerLogDto selectJobTriggerLogDtoById(long id) {
        return jobTriggerLogDao.selectJobTriggerLogDtoById(id);
    }

    /**
     * 根据jobId和dagTriggerId获取JobTriggerLogDto
     *
     * @param jobId
     * @param dagTriggerId
     * @return
     */
    public JobTriggerLogDto selectJobTriggerLogDtoByJobAndDag(long jobId, long dagTriggerId, boolean lock) {
        return jobTriggerLogDao.selectJobTriggerLogDtoByJobAndDag(jobId, dagTriggerId, lock);
    }


    /**
     * 类似乐观锁方式进行更新.
     * @param jobTriggerLogDto
     * @param expectStatus
     * @return
     */
    public int updateJobTriggerLogWithAssignedStatus(JobTriggerLogDto jobTriggerLogDto,int expectStatus) {
        return jobTriggerLogDao.updateJobTriggerLogWithAssignedStatus(jobTriggerLogDto,expectStatus);
    }


    public JobTriggerLogDto generateJobTriggerLogDto(long jobId, long dagId, long dagTriggerId, int status,String centerIp, String executorIp, String param) {
        JobTriggerLogDto jobTriggerLogDto = new JobTriggerLogDto();
        jobTriggerLogDto.setDagId(dagId);
        jobTriggerLogDto.setDagTriggerId(dagTriggerId);
        jobTriggerLogDto.setJobId(jobId);
        jobTriggerLogDto.setStartTime(new Date());
        jobTriggerLogDto.setStatus(status);
        jobTriggerLogDto.setCenterIp(centerIp);
        jobTriggerLogDto.setExecutorIp(executorIp);
        jobTriggerLogDto.setShardingCnt(1);
        jobTriggerLogDto.setShardingIdx(1);
        jobTriggerLogDto.setParam(param);
        return jobTriggerLogDto;
    }



    private JobInfo generateJobInfo(JobDto jobDto, JobTriggerLogDto jobTriggerLogDto) {
        JobInfo jobInfo = new JobInfo();
        jobInfo.setJobName(jobDto.getJobName());
        jobInfo.setJobTriggerLogId(jobTriggerLogDto.getId());
        jobInfo.setLaunchCommand(jobDto.getLaunchCommand());
        jobInfo.setParam(jobTriggerLogDto.getParam());
        return jobInfo;
    }

}
