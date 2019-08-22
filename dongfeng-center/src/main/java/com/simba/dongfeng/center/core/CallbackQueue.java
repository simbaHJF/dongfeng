package com.simba.dongfeng.center.core;

import com.simba.dongfeng.common.pojo.Callback;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * DATE:   2019-08-19 20:51
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class CallbackQueue {
    private LinkedBlockingDeque<Callback> callbackQueue = new LinkedBlockingDeque<>();


    public synchronized Callback takeHead() throws InterruptedException {
        return callbackQueue.takeFirst();
    }


    public synchronized void addTailIfNotEnqueue(Callback callBack) {
        boolean hasEnqueue = false;
        for (Callback cur : callbackQueue) {
            if (callBack.getJobTriggerLogId() == cur.getJobTriggerLogId()) {
                hasEnqueue = true;
                break;
            }
        }
        if (!hasEnqueue) {
            callbackQueue.addLast(callBack);
        }
    }

}
