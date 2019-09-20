package com.simba.dongfeng.executor.thread;

import com.simba.dongfeng.common.enums.RespCodeEnum;
import com.simba.dongfeng.common.pojo.Callback;
import com.simba.dongfeng.common.pojo.RespDto;
import com.simba.dongfeng.common.util.HttpClient;
import com.simba.dongfeng.executor.core.CallbackNotifyFailQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * DATE:   2019/9/20 16:12
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class CallbackNotifyFailRetryHelper {
    private Logger logger = LoggerFactory.getLogger(CallbackNotifyFailRetryHelper.class);
    private boolean isRunning = true;
    private String executorIp;
    private CallbackNotifyFailQueue callbackNotifyFailQueue;
    private List<String> dongfengCenterAddrList;

    private int interval = 2; //minute

    private Thread callbackNotifyFailRetryThread;

    public CallbackNotifyFailRetryHelper(String executorIp, CallbackNotifyFailQueue callbackNotifyFailQueue, List<String> dongfengCenterAddrList) {
        this.executorIp = executorIp;
        this.callbackNotifyFailQueue = callbackNotifyFailQueue;
        this.dongfengCenterAddrList = dongfengCenterAddrList;
    }

    public void start() {
        callbackNotifyFailRetryThread = new Thread() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        List<Callback> list = new ArrayList<>();
                        callbackNotifyFailQueue.drain(list);
                        for (Callback callback : list) {
                            boolean notifySucc = false;
                            for (String host : dongfengCenterAddrList) {
                                RespDto respDto = null;
                                try {
                                    respDto = HttpClient.sendPost(host, "/dongfeng/callback", callback, 5000);

                                    if (respDto.getCode() != RespCodeEnum.SUCC.getCode()) {
                                        logger.warn("callbackNotifyFailRetry.resp code is not succ.host:" + host + ",callback:" + callback + ",resp:" + respDto);
                                        continue;
                                    }
                                    notifySucc = true;
                                    break;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    logger.warn("send callbackNotifyFailRetry request err.host:" + host + ",callback:" + callback + ",resp:" + respDto, e);
                                }
                            }
                            if (!notifySucc) {
                                callbackNotifyFailQueue.addTail(callback);
                            }
                        }

                        TimeUnit.MINUTES.sleep(interval);

                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("CallbackNotifyFailRetryHelper ## callbackNotifyFailRetryThread err.", e);
                    }
                }
            }
        };

        callbackNotifyFailRetryThread.setName("CallbackNotifyFailRetryHelper ## callbackNotifyFailRetryThread");
        callbackNotifyFailRetryThread.setDaemon(true);
        callbackNotifyFailRetryThread.start();
    }


    public void stop() {
        isRunning = false;
        if (callbackNotifyFailRetryThread != null && callbackNotifyFailRetryThread.getState() != Thread.State.TERMINATED) {
            // interrupt and wait
            callbackNotifyFailRetryThread.interrupt();
            try {
                callbackNotifyFailRetryThread.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
