package com.simba.dongfeng.executor.core;

import com.simba.dongfeng.common.enums.JobStatusEnum;
import com.simba.dongfeng.common.pojo.Callback;
import com.simba.dongfeng.common.pojo.JobInfo;
import com.simba.dongfeng.executor.pojo.JobRecord;
import com.simba.dongfeng.executor.pojo.JobRecordPool;
import com.simba.dongfeng.executor.util.ProcessUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * DATE:   2019/8/24 11:43
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class WorkWarpper implements Runnable {
    private Logger logger = LoggerFactory.getLogger(WorkWarpper.class);
    private JobInfo jobInfo;
    private CallbackQueue callbackQueue;
    private JobRecordPool jobRecordPool;




    public WorkWarpper(JobInfo jobInfo, CallbackQueue callbackQueue, JobRecordPool jobRecordPool) {
        this.jobInfo = jobInfo;
        this.callbackQueue = callbackQueue;
        this.jobRecordPool = jobRecordPool;
    }

    @Override
    public void run() {
        JobRecord jobRecord = jobRecordPool.getJobRecord(jobInfo.getJobTriggerLogId());
        try {
            if (jobRecord  != null) {
                Process process = Runtime.getRuntime().exec(jobInfo.getLaunchCommand() + " " + jobInfo.getParam());
                jobRecord.setProcess(process);
                /*ProcessUtil.readProcessOutput(process);*/
                int exitValue = process.waitFor();
                Callback callback = new Callback();
                callback.setJobTriggerLogId(jobInfo.getJobTriggerLogId());
                callback.setShardingIdx(jobInfo.getShardingIdx());
                if (exitValue == 0) {
                    callback.setJobExecRs(JobStatusEnum.SUCC.getValue());
                } else {
                    callback.setJobExecRs(JobStatusEnum.FAIL.getValue());
                }
                jobRecord.setEndTime(new Date());
                logger.info("job exec over.jobInfo:" + jobInfo + ",callback:" + callback);
                callbackQueue.addTail(callback);
            }
        } catch (Exception e) {
            logger.error("job exec err.jobName:" + jobInfo.getJobName() + ",launchCommond:" + jobInfo.getLaunchCommand(), e);
            e.printStackTrace();
        }
    }

    public JobInfo getJobInfo() {
        return jobInfo;
    }
}
