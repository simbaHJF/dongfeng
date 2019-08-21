package com.simba.dongfeng.executor.core;

import com.simba.dongfeng.common.pojo.Callback;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * DATE:   2019-08-21 14:17
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class CallbackQueue {
    private LinkedBlockingDeque<Callback> callbackQueue = new LinkedBlockingDeque<>();


    public Callback takeHead() throws InterruptedException {
        return callbackQueue.takeFirst();
    }

    public Callback takeTail() throws InterruptedException {
        return callbackQueue.takeLast();
    }

    public synchronized Callback peekHead() {
        return callbackQueue.peekFirst();
    }

    public synchronized Callback peekTail() {
        return callbackQueue.peekLast();
    }

    public synchronized Callback pollHead() {
        return callbackQueue.pollFirst();
    }

    public synchronized Callback pollTail() {
        return callbackQueue.peekLast();
    }

    public synchronized void addHead(Callback callBack) {
        callbackQueue.addFirst(callBack);
    }

    public synchronized void addTail(Callback callBack) {
        callbackQueue.addLast(callBack);
    }

    public synchronized void addAllToTail(List<Callback> callbacks) {
        callbackQueue.addAll(callbacks);
    }

    public synchronized int queueSize() {
        return callbackQueue.size();
    }

}
