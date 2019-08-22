package com.simba.dongfeng.executor.core;

import com.simba.dongfeng.common.pojo.JobInfo;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * DATE:   2019-08-21 14:00
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class JobQueue {
    private LinkedBlockingDeque<JobInfo> jobInfoQueue = new LinkedBlockingDeque<>(5);


    public synchronized JobInfo takeHead() throws InterruptedException {
        return jobInfoQueue.takeFirst();
    }

    public synchronized void addTailIfNotEnqueue(JobInfo jobInfo) {
        boolean hasEnqueue = false;
        for (JobInfo cur : jobInfoQueue) {
            if (jobInfo.getJobTriggerLogId() == cur.getJobTriggerLogId()) {
                hasEnqueue = true;
                break;
            }
        }
        if (!hasEnqueue) {
            jobInfoQueue.addLast(jobInfo);
        }
    }

    public synchronized int queueSize() {
        return jobInfoQueue.size();
    }
}
