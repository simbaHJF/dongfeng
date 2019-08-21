package com.simba.dongfeng.center.core;

import com.simba.dongfeng.common.pojo.CallbackDto;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * DATE:   2019-08-19 20:51
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class CallbackQueue {
    private LinkedBlockingDeque<CallbackDto> callbackQueue = new LinkedBlockingDeque<>();


    public CallbackDto takeHead() throws InterruptedException {
        return callbackQueue.takeFirst();
    }

    public CallbackDto takeTail() throws InterruptedException {
        return callbackQueue.takeLast();
    }

    public synchronized CallbackDto peekHead() {
        return callbackQueue.peekFirst();
    }

    public synchronized CallbackDto peekTail() {
        return callbackQueue.peekLast();
    }

    public synchronized CallbackDto pollHead() {
        return callbackQueue.pollFirst();
    }

    public synchronized CallbackDto pollTail() {
        return callbackQueue.peekLast();
    }

    public synchronized void addHead(CallbackDto callBackDto) {
        callbackQueue.addFirst(callBackDto);
    }

    public synchronized void addTail(CallbackDto callBackDto) {
        callbackQueue.addLast(callBackDto);
    }

    public synchronized void addAllToTail(List<CallbackDto> callbackDtos) {
        callbackQueue.addAll(callbackDtos);
    }

    public synchronized int queueSize() {
        return callbackQueue.size();
    }
}
