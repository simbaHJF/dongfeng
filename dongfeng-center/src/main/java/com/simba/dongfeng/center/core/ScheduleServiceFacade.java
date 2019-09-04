package com.simba.dongfeng.center.core;

import com.alibaba.fastjson.JSON;
import com.simba.dongfeng.center.core.route.ExecutorRouterStg;
import com.simba.dongfeng.center.dao.*;
import com.simba.dongfeng.center.enums.DagExecStatusEnum;
import com.simba.dongfeng.center.enums.DagTriggerTypeEnum;
import com.simba.dongfeng.center.enums.ExecutorRouterStgEnum;
import com.simba.dongfeng.center.pojo.*;
import com.simba.dongfeng.common.enums.JobStatusEnum;
import com.simba.dongfeng.common.enums.RespCodeEnum;
import com.simba.dongfeng.common.pojo.JobInfo;
import com.simba.dongfeng.common.pojo.RespDto;
import com.simba.dongfeng.common.util.HttpClient;
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
public class ScheduleServiceFacade {
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


    private Comparator<DagDto> comparator = Comparator.comparing(DagDto::getTriggerTime);


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
        List<DagDto> dagsInFetchTimeWindow = new ArrayList<>();
        Iterator<DagDto> iterator = dagDtoList.iterator();
        while (iterator.hasNext()) {
            DagDto dagDto = iterator.next();
            try {
                CronExpression cronExpression = new CronExpression(dagDto.getDagCron());
                Date nextValidTime = cronExpression.getNextValidTimeAfter(nowTimeline);
                while (nextValidTime.before(endTimeline)) {
                    DagDto tmp = new DagDto();
                    tmp.setId(dagDto.getId());
                    tmp.setDagName(dagDto.getDagName());
                    tmp.setDagGroup(dagDto.getDagGroup());
                    tmp.setDagCron(dagDto.getDagCron());
                    tmp.setStatus(dagDto.getStatus());
                    tmp.setTriggerTime(nextValidTime);
                    tmp.setParam(dagDto.getParam());
                    tmp.setTriggerType(DagTriggerTypeEnum.CRON.getValue());
                    dagsInFetchTimeWindow.add(tmp);
                    nextValidTime = cronExpression.getNextValidTimeAfter(nextValidTime);
                }
                dagDao.updateDagTriggerTime(dagDto.getId(), nextValidTime);
            } catch (ParseException e) {
                System.out.println("parse dag trigger cron err.dag:" + dagDto);
                logger.error("parse dag trigger cron err.dag:" + dagDto, e);
                //TODO:  ALARM,  DO NOT THROW EXCEPTION
            }
        }
        dagsInFetchTimeWindow.sort(comparator);
        return dagsInFetchTimeWindow;
    }

    public boolean insertOrUploadJobTriggerLogWhenTriggerNewJob(JobTriggerLogDto jobTriggerLog) {
        JobTriggerLogDto jobLog = jobTriggerLogDao.selectJobTriggerLogDtoByJobAndDag(jobTriggerLog.getJobId(), jobTriggerLog.getDagTriggerId(), false);
        if (jobLog == null) {
            jobTriggerLogDao.inserJobTriggerLog(jobTriggerLog);
            return true;
        } else {
            if (jobLog.getStatus() != JobStatusEnum.INITIAL.getValue()) {
                logger.info("jobTriggerLog has been handled.jobTriggerLog:" + jobTriggerLog);
                return false;
            }
            jobTriggerLogDao.updateJobTriggerLogWithAssignedCenter(jobTriggerLog);
            return true;
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
        String host = executorDto.getExecutorIp() + ":" + executorDto.getExecutorPort();
        String respStr = HttpClient.sendPost(host, "/dongfengexecutor/job/trigger", jobInfo, 5000);
        RespDto respDto = JSON.parseObject(respStr, RespDto.class);
        if (respDto.getCode() != RespCodeEnum.SUCC.getCode()) {
            logger.error("dispatch err.jobInfo:" + jobInfo + ",executorDto:" + executorDto + ",respDto:" + respDto);
            throw new RuntimeException("dispatch err.jobInfo:" + jobInfo + ",executorDto:" + executorDto + ",respDto:" + respDto);
        }
    }


    /**
     * 写调度日志,调度job(TASK_NODE节点)
     * @param jobDto
     * @param dagTriggerLogDto
     * @param jobScheduleRetryTime
     * @param centerIp
     */
    public void scheduleJob(JobDto jobDto, DagTriggerLogDto dagTriggerLogDto, int jobScheduleRetryTime, String centerIp) {
        int scheduleCnt = 0;
        ExecutorDto executor = null;
        JobTriggerLogDto curJobTriggerLog = generateJobTriggerLogDto(jobDto.getId(), dagTriggerLogDto.getDagId(), dagTriggerLogDto.getId(), JobStatusEnum.INITIAL.getValue(), centerIp, null, dagTriggerLogDto.getParam());
        while (true) {
            try {
                /**
                 * 先写调度日志,再调度
                 */
                executor = this.route(jobDto);
                curJobTriggerLog.setExecutorIp(executor.getExecutorIp());

                //无锁化,db中job_trigger_log表限制job_id+dag_trigger_id的唯一索引,保证同一dag_trigger_id下不会重复插入某个job_trigger_log
                // 多center节点时,由于网络原因,存在executor向每个center都发送了一遍任务处理回调结果,造成多个center想执行触发后续job任务,写jobLog的逻辑.
                // 重复插入后会由于唯一索引冲突报错进入catch模块
                // 再加上centerIp字段限制,只有成功写入后续任务的jobLog的center能够处理后续任务状态设置,时间设置等操作.
                boolean rs = insertOrUploadJobTriggerLogWhenTriggerNewJob(curJobTriggerLog);
                if (rs == true) {
                    JobInfo jobInfo = this.generateJobInfo(jobDto, curJobTriggerLog);
                    this.dispatch(jobInfo, executor);
                    curJobTriggerLog.setStatus(JobStatusEnum.RUNNING.getValue());
                    jobTriggerLogDao.updateJobTriggerLog(curJobTriggerLog);
                }
                break;
            } catch (Exception e) {
                logger.info("job schedule err.", e);
                if (scheduleCnt++ < jobScheduleRetryTime) {
                    System.out.println("job schedule retry:" + scheduleCnt + ",job:" + jobDto.toString() + ",executor:" + executor.toString());
                    logger.info("job schedule retry:" + scheduleCnt + ",job:" + jobDto.toString() + ",executor:" + executor.toString());
                    continue;
                } else {
                    JobTriggerLogDto jobTriggerLog = this.selectJobTriggerLogDtoByJobAndDag(curJobTriggerLog.getJobId(), curJobTriggerLog.getDagTriggerId(), false);
                    if (jobTriggerLog.getStatus() == JobStatusEnum.INITIAL.getValue() && jobTriggerLog.getCenterIp().equals(centerIp)) {
                        curJobTriggerLog.setStatus(JobStatusEnum.FAIL.getValue());
                        curJobTriggerLog.setEndTime(new Date());
                        jobTriggerLogDao.updateJobTriggerLogWithAssignedCenter(curJobTriggerLog);

                        dagTriggerLogDto.setStatus(DagExecStatusEnum.FAIL.getValue());
                        dagTriggerLogDto.setEndTime(new Date());
                        dagTriggerLogDao.updateDagTriggerLog(dagTriggerLogDto);

                        //TODO alarm
                        logger.error("job schedule failed.job:" + jobDto, e);
                    }
                    System.out.println("job schedule retry over.jobLog has been handled or jobLog is not belong to cur center." +
                            "jobLog status:" + jobTriggerLog.getStatus() + ",corresponding centerIp:" + jobTriggerLog.getCenterIp());
                    logger.info("job schedule retry over.jobLog has been handled or jobLog is not belong to cur center." +
                            "jobLog status:" + jobTriggerLog.getStatus() + ",corresponding centerIp:" + jobTriggerLog.getCenterIp());
                    break;
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
     *
     * @param jobTriggerLogDto
     * @param expectStatus
     * @return
     */
    public int updateJobTriggerLogWithAssignedStatus(JobTriggerLogDto jobTriggerLogDto, int expectStatus) {
        return jobTriggerLogDao.updateJobTriggerLogWithAssignedStatus(jobTriggerLogDto, expectStatus);
    }


    public JobTriggerLogDto generateJobTriggerLogDto(long jobId, long dagId, long dagTriggerId, int status, String centerIp, String executorIp, String param) {
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
        jobInfo.setShardingIdx(1);
        return jobInfo;
    }

}
