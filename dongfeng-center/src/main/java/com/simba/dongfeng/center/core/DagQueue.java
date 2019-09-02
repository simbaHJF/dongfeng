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


    public DagDto peekTail() {
        return dagQueue.peekLast();
    }


    public void addHead(DagDto dagDto) {
        dagQueue.addFirst(dagDto);
    }

    public void addTail(DagDto dagDto) {
        dagQueue.addLast(dagDto);
    }

    public void addAllToTail(List<DagDto> dagDtos) {
        dagQueue.addAll(dagDtos);
    }

    public synchronized int queueSize() {
        return dagQueue.size();
    }

}
