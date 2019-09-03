package com.simba.dongfeng.executor.core;

import com.simba.dongfeng.common.enums.JobStatusEnum;
import com.simba.dongfeng.common.pojo.Callback;
import com.simba.dongfeng.common.pojo.JobInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DATE:   2019/8/24 11:43
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class WorkWarpper implements Runnable {
    private Logger logger = LoggerFactory.getLogger(WorkWarpper.class);
    private JobInfo jobInfo;
    CallbackQueue callbackQueue;

    public WorkWarpper(JobInfo jobInfo, CallbackQueue callbackQueue) {
        this.jobInfo = jobInfo;
        this.callbackQueue = callbackQueue;
    }

    @Override
    public void run() {
        try {
            Process process = Runtime.getRuntime().exec(jobInfo.getLaunchCommand() + " " + jobInfo.getParam());
            System.out.println("workWarper exec");
            int exitValue = process.waitFor();
            Callback callback = new Callback();
            callback.setJobTriggerLogId(jobInfo.getJobTriggerLogId());
            callback.setShardingIdx(jobInfo.getShardingIdx());
            if (exitValue == 0) {
                callback.setJobExecRs(JobStatusEnum.SUCC.getValue());
            } else {
                callback.setJobExecRs(JobStatusEnum.FAIL.getValue());
            }
            System.out.println("callbackQueue.addTail");
            callbackQueue.addTail(callback);
        } catch (Exception e) {
            logger.error("job exec err.jobName:" + jobInfo.getJobName() + ",launchCommond:" + jobInfo.getLaunchCommand());
            e.printStackTrace();
        }
    }
}
