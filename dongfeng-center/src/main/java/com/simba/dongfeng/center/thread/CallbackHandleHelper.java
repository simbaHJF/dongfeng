package com.simba.dongfeng.center.thread;

import com.simba.dongfeng.center.core.CallbackQueue;
import com.simba.dongfeng.center.core.ScheduleServiceFacade;
import com.simba.dongfeng.center.enums.DagExecStatusEnum;
import com.simba.dongfeng.center.enums.JobTypeEnum;
import com.simba.dongfeng.center.pojo.DagDto;
import com.simba.dongfeng.center.pojo.DagTriggerLogDto;
import com.simba.dongfeng.center.pojo.JobDto;
import com.simba.dongfeng.center.pojo.JobTriggerLogDto;
import com.simba.dongfeng.common.enums.JobStatusEnum;
import com.simba.dongfeng.common.pojo.Callback;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DATE:   2019-08-19 21:17
 * AUTHOR: simba.hjf
 * DESC:    处理job执行结果回调,并进行子job调度
 **/
public class CallbackHandleHelper {
    private Logger logger = LoggerFactory.getLogger(CallbackHandleHelper.class);

    private boolean isRunning = true;

    private Thread callBackHandleThread;
    private CallbackQueue callbackQueue;
    private ScheduleServiceFacade scheduleServiceFacade;

    public CallbackHandleHelper(CallbackQueue callbackQueue, ScheduleServiceFacade scheduleServiceFacade) {
        this.callbackQueue = callbackQueue;
        this.scheduleServiceFacade = scheduleServiceFacade;
    }

    public void start() {
        callBackHandleThread = new Thread() {
            @Override
            public void run() {
                InetAddress addr = null;
                try {
                    addr = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                while (isRunning) {
                    try {
                        Callback callback = callbackQueue.takeHead();
                        logger.info("callBackHandleThread ## callbackQueue head callback:" + callback);

                        JobTriggerLogDto jobTriggerLog = scheduleServiceFacade.selectJobTriggerLogDtoById(callback.getJobTriggerLogId());
                        logger.info("callback corresponding jobLog:" + jobTriggerLog);

                        if (jobTriggerLog == null) {
                            logger.error("job callback.corresponding jobTriggerLog is null.jobLogId:" + callback.getJobTriggerLogId());
                            continue;
                        }

                        if (jobTriggerLog.getStatus() == JobStatusEnum.SUCC.getValue() || jobTriggerLog.getStatus() == JobStatusEnum.FAIL.getValue()) {
                            logger.info("job callback has been handled.corresponding jobTriggerLog is finished.jobTriggerLog:" + jobTriggerLog);
                            continue;
                        }
                        jobTriggerLog.setEndTime(new Date());
                        jobTriggerLog.setStatus(callback.getJobExecRs());
                        jobTriggerLog.setExecutorIp(callback.getExecutorIp());

                        // 无锁化,更新回调任务执行结果.
                        List<Integer> expectStatus = new ArrayList<>();
                        expectStatus.add(JobStatusEnum.INITIAL.getValue());
                        expectStatus.add(JobStatusEnum.RUNNING.getValue());
                        int updateRs = scheduleServiceFacade.updateJobTriggerLogWithAssignedStatus(jobTriggerLog, expectStatus);
                        if (updateRs != 1) {
                            logger.info("job callback has been handled.jobTriggerLog:" + jobTriggerLog);
                            continue;
                        }

                        DagTriggerLogDto dagTriggerLog = scheduleServiceFacade.selectDagTriggerLogById(jobTriggerLog.getDagTriggerId());
                        if (dagTriggerLog.getStatus() == DagExecStatusEnum.FAIL.getValue()) {
                            logger.error("dag trigger has failed due to other failed job.it will not trigger subsequent job");
                            continue;
                        }
                        List<JobDto> childJobList = scheduleServiceFacade.selectChildJobList(jobTriggerLog.getJobId());
                        for (JobDto childJobDto : childJobList) {
                            List<JobDto> parentJobList = scheduleServiceFacade.selectParentJobList(childJobDto.getId());
                            boolean allParentJobsCompleteFlag = true;
                            boolean allParentJobsSuccFlag = true;
                            for (JobDto parentJob : parentJobList) {
                                JobTriggerLogDto parentJobTriggerLog = scheduleServiceFacade.selectJobTriggerLogDtoByJobAndDag(parentJob.getId(), dagTriggerLog.getId(), false);
                                if (parentJobTriggerLog.getStatus() == JobStatusEnum.RUNNING.getValue()) {
                                    allParentJobsCompleteFlag = false;

                                } else if (parentJobTriggerLog.getStatus() == JobStatusEnum.FAIL.getValue()) {
                                    allParentJobsSuccFlag = false;

                                }
                            }
                            if (allParentJobsCompleteFlag) {
                                //所有父任务执行完成(不论是否成功)

                                logger.info("callBackHandle,jobId:" + jobTriggerLog.getJobId() + ",childJob:" + childJobDto + ",parentJobs all completed.");
                                if (allParentJobsSuccFlag) {
                                    //父job全部成功

                                    logger.info("callBackHandle,jobId:" + jobTriggerLog.getJobId() + ",childJob:" + childJobDto + ",parentJobs all succ,will trigge next job.");
                                    JobTypeEnum jobTypeEnum = JobTypeEnum.getJobTypeEnumByValue(childJobDto.getJobType());
                                    if (jobTypeEnum == JobTypeEnum.END_NODE) {
                                        //子job是结束node

                                        dagTriggerLog.setEndTime(new Date());
                                        dagTriggerLog.setStatus(DagExecStatusEnum.SUCC.getValue());
                                        scheduleServiceFacade.updateDagTriggerLog(dagTriggerLog);

                                        JobTriggerLogDto endJobTriggerLog = scheduleServiceFacade.generateJobTriggerLogDto(childJobDto.getId(),childJobDto.getJobName(), dagTriggerLog.getDagId(), dagTriggerLog.getId(), JobStatusEnum.SUCC.getValue(), addr.getHostAddress(), addr.getHostAddress(), dagTriggerLog.getParam());
                                        endJobTriggerLog.setEndTime(new Date());
                                        scheduleServiceFacade.insertJobTriggerLog(endJobTriggerLog);

                                        logger.info("callBackHandle,dag complete succ.dagTriggerLog:" + dagTriggerLog);
                                    } else {
                                        //子job不是结束node
                                        logger.info("callBackHandle,schedule childJob:" + childJobDto);
                                        scheduleServiceFacade.scheduleJob(childJobDto, dagTriggerLog, 2, addr.getHostAddress());
                                    }
                                } else {
                                    //父job有失败

                                    JobTypeEnum jobTypeEnum = JobTypeEnum.getJobTypeEnumByValue(childJobDto.getJobType());
                                    if (jobTypeEnum == JobTypeEnum.END_NODE) {
                                        //子job是结束node

                                        dagTriggerLog.setEndTime(new Date());
                                        dagTriggerLog.setStatus(DagExecStatusEnum.FAIL.getValue());
                                        scheduleServiceFacade.updateDagTriggerLog(dagTriggerLog);
                                        scheduleServiceFacade.sendFailedAlarm(dagTriggerLog);
                                        try {
                                            addr = InetAddress.getLocalHost();
                                        } catch (UnknownHostException e) {
                                            e.printStackTrace();
                                        }
                                        JobTriggerLogDto endJobTriggerLog = scheduleServiceFacade.generateJobTriggerLogDto(childJobDto.getId(),childJobDto.getJobName(), dagTriggerLog.getDagId(), dagTriggerLog.getId(), JobStatusEnum.FAIL.getValue(), addr.getHostAddress(), addr.getHostAddress(), dagTriggerLog.getParam());
                                        endJobTriggerLog.setEndTime(new Date());
                                        scheduleServiceFacade.insertJobTriggerLog(endJobTriggerLog);

                                        logger.info("callBackHandle,dag complete with failed job.dagTriggerLog:" + dagTriggerLog);
                                    } else {
                                        //子job不是结束node,不再调度子job,dagTriggerLog设置失败状态
                                        dagTriggerLog.setEndTime(new Date());
                                        dagTriggerLog.setStatus(DagExecStatusEnum.FAIL.getValue());
                                        scheduleServiceFacade.updateDagTriggerLog(dagTriggerLog);
                                        scheduleServiceFacade.sendFailedAlarm(dagTriggerLog);
                                    }
                                }

                            } else {
                                //父任务未全部执行完成
                                logger.info("callBackHandle,jobId:" + jobTriggerLog.getJobId() + ",childJob:" + childJobDto + ",parentJobs have not all completed.");
                            }
                        }
                    } catch (Exception e) {
                        logger.error("callBackHandleThread err.", e);
                        e.printStackTrace();
                    }
                }
            }
        };

        callBackHandleThread.setDaemon(true);
        callBackHandleThread.setName("CallbackHandleHelper#callBackHandleThread");
        callBackHandleThread.start();
    }

    public void stop() {
        isRunning = false;
        if (callBackHandleThread != null && callBackHandleThread.getState() != Thread.State.TERMINATED) {
            // interrupt and wait
            callBackHandleThread.interrupt();
            try {
                callBackHandleThread.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

}
