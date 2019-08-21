package com.simba.dongfeng.center.core;

import com.simba.dongfeng.center.pojo.DagDto;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * DATE:   2019-08-16 10:12
 * AUTHOR: simba.hjf
 * DESC:    dag调度队列,封装为线程安全队列
 **/
public class DagQueue {
    private LinkedBlockingDeque<DagDto> dagQueue = new LinkedBlockingDeque<>();


    public DagDto takeHead() throws InterruptedException {
        return dagQueue.takeFirst();
    }

    public DagDto takeTail() throws InterruptedException {
        return dagQueue.takeLast();
    }

    public synchronized DagDto peekHead() {
        return dagQueue.peekFirst();
    }

    public synchronized DagDto peekTail() {
        return dagQueue.peekLast();
    }

    public synchronized DagDto pollHead() {
        return dagQueue.pollFirst();
    }

    public synchronized DagDto pollTail() {
        return dagQueue.peekLast();
    }

    public synchronized void addHead(DagDto dagDto) {
        dagQueue.addFirst(dagDto);
    }

    public synchronized void addTail(DagDto dagDto) {
        dagQueue.addLast(dagDto);
    }

    public synchronized void addAllToTail(List<DagDto> dagDtos) {
        dagQueue.addAll(dagDtos);
    }

    public synchronized int queueSize() {
        return dagQueue.size();
    }

}
