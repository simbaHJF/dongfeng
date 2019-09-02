package com.simba.dongfeng.executor.thread;

import com.simba.dongfeng.common.pojo.Callback;
import com.simba.dongfeng.common.util.HttpClient;
import com.simba.dongfeng.executor.core.CallbackQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * DATE:   2019/8/23 14:15
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class CallbackNotifyHelper {
    private Logger logger = LoggerFactory.getLogger(CallbackNotifyHelper.class);
    private boolean isRunning = true;
    private CallbackQueue callbackQueue;
    private List<String> dongfengCenterAddrList;

    private Thread callbackNotifyThread;

    public CallbackNotifyHelper(CallbackQueue callbackQueue, List<String> dongfengCenterAddrList) {
        this.callbackQueue = callbackQueue;
        this.dongfengCenterAddrList = dongfengCenterAddrList;
    }

    public void start() {
        callbackNotifyThread = new Thread() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        Callback callback = callbackQueue.takeHead();
                        for (String host : dongfengCenterAddrList) {
                            try {
                                HttpClient.sendPost(host, "/callback", callback, 5000);
                            } catch (Exception e) {
                                logger.error("send callback request err.host:" + host + ",callback:" + callback);
                                continue;
                            }
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        callbackNotifyThread.setDaemon(true);
        callbackNotifyThread.setName("CallbackNotifyHelper#callbackNotifyThread");
        callbackNotifyThread.start();
    }


    public void stop() {
        isRunning = false;
        if (callbackNotifyThread != null && callbackNotifyThread.getState() != Thread.State.TERMINATED) {
            // interrupt and wait
            callbackNotifyThread.interrupt();
            try {
                callbackNotifyThread.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
