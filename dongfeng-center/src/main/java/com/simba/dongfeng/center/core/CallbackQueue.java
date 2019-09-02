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


    public Callback takeHead() throws InterruptedException {
        return callbackQueue.takeFirst();
    }


    public void addTail(Callback callBack) {
        callbackQueue.addLast(callBack);
    }

}
