package com.simba.dongfeng.executor.thread;

import com.simba.dongfeng.common.enums.JobStatusEnum;
import com.simba.dongfeng.common.pojo.Callback;
import com.simba.dongfeng.common.pojo.JobInfo;
import com.simba.dongfeng.executor.core.CallbackQueue;
import com.simba.dongfeng.executor.core.JobQueue;
import org.apache.catalina.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * DATE:   2019-08-22 14:01
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class JobExecHelper {
    private Logger logger = LoggerFactory.getLogger(JobExecHelper.class);
    //任务执行线程(创建进程,执行任务)
    private ExecutorService threadPoolExecutor = new ThreadPoolExecutor(3, 5, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>());

    private JobQueue jobQueue;
    private CallbackQueue callbackQueue;


    private class WorkWarpper implements Runnable {
        private JobInfo jobInfo;

        public WorkWarpper(JobInfo jobInfo) {
            this.jobInfo = jobInfo;
        }

        @Override
        public void run() {
            try {
                Process process = Runtime.getRuntime().exec(jobInfo.getLaunchCommand());
                int exitValue = process.waitFor();
                    Callback callback = new Callback();
                callback.setJobTriggerLogId(jobInfo.getJobTriggerLogId());
                callback.setShardingIdx(jobInfo.getShardingIdx());
                if (exitValue == 0) {
                    callback.setJobExecRs(JobStatusEnum.SUCC.getValue());
                } else {
                    callback.setJobExecRs(JobStatusEnum.FAIL.getValue());
                }
                callbackQueue.addTail(callback);
            } catch (Exception e) {
                logger.error("job exec err.jobName:" + jobInfo.getJobName() + ",launchCommond:" + jobInfo.getLaunchCommand());
                e.printStackTrace();
            }
        }
    }

}
