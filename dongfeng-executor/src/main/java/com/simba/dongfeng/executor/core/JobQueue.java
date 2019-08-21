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
    private LinkedBlockingDeque<JobInfo> jobInfoQueue = new LinkedBlockingDeque<>();


    public JobInfo takeHead() throws InterruptedException {
        return jobInfoQueue.takeFirst();
    }

    public JobInfo takeTail() throws InterruptedException {
        return jobInfoQueue.takeLast();
    }

    public synchronized JobInfo peekHead() {
        return jobInfoQueue.peekFirst();
    }

    public synchronized JobInfo peekTail() {
        return jobInfoQueue.peekLast();
    }

    public synchronized JobInfo pollHead() {
        return jobInfoQueue.pollFirst();
    }

    public synchronized JobInfo pollTail() {
        return jobInfoQueue.peekLast();
    }

    public synchronized void addHead(JobInfo jobInfo) {
        jobInfoQueue.addFirst(jobInfo);
    }

    public synchronized void addTail(JobInfo jobInfo) {
        jobInfoQueue.addLast(jobInfo);
    }

    public synchronized void addAllToTail(List<JobInfo> jobInfos) {
        jobInfoQueue.addAll(jobInfos);
    }

    public synchronized int queueSize() {
        return jobInfoQueue.size();
    }
}
