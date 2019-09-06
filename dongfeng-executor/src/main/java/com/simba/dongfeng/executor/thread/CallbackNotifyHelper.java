package com.simba.dongfeng.executor.thread;

import com.alibaba.fastjson.JSON;
import com.simba.dongfeng.common.enums.RespCodeEnum;
import com.simba.dongfeng.common.pojo.Callback;
import com.simba.dongfeng.common.pojo.RespDto;
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
    private String executorIp;
    private CallbackQueue callbackQueue;
    private List<String> dongfengCenterAddrList;

    private Thread callbackNotifyThread;

    public CallbackNotifyHelper(String executorIp, CallbackQueue callbackQueue, List<String> dongfengCenterAddrList) {
        this.executorIp = executorIp;
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
                        callback.setExecutorIp(executorIp);
                        logger.info("callbackNotifyThread ## callbackQueue head callback:" + callback);
                        System.out.println("callbackNotifyThread ## callbackQueue head callback:" + callback);
                        int callbackNotifySendFailCnt = 0;
                        for (String host : dongfengCenterAddrList) {
                            RespDto respDto = null;
                            try {
                                respDto = HttpClient.sendPost(host, "/dongfeng/callback", callback, 5000);

                                if (respDto.getCode() != RespCodeEnum.SUCC.getCode()) {
                                    logger.warn("send callback.resp code is not succ.host:" + host + ",callback:" + callback + ",resp:" + respDto);
                                    callbackNotifySendFailCnt++;
                                    continue;
                                }
                                break;
                            } catch (Exception e) {
                                e.printStackTrace();
                                logger.warn("send callback request err.host:" + host + ",callback:" + callback + ",resp:" + respDto, e);
                                callbackNotifySendFailCnt++;
                            }
                        }
                        if (callbackNotifySendFailCnt == dongfengCenterAddrList.size()) {
                            logger.error("send callback request err.all center fail,callback:" + callback);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("CallbackNotifyHelper ## callbackNotifyThread err.", e);
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
