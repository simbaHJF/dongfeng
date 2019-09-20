package com.simba.dongfeng.executor.core;

import com.simba.dongfeng.common.pojo.Callback;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * DATE:   2019/9/20 15:57
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class CallbackNotifyFailQueue {
    private LinkedBlockingDeque<Callback> callbackQueue = new LinkedBlockingDeque<>();

    public void drain(List<Callback> list) throws InterruptedException {
        Callback callback = callbackQueue.take();
        callbackQueue.drainTo(list);
        list.add(callback);
    }

    public void addTail(Callback callBack) {
        callbackQueue.addLast(callBack);
    }
}
