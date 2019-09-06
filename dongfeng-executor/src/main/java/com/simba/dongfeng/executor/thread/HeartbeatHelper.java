package com.simba.dongfeng.executor.thread;

import com.simba.dongfeng.common.pojo.ExecutorHeartbeatInfo;
import com.simba.dongfeng.executor.core.ExecutorServiceFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * DATE:   2019-08-21 14:21
 * AUTHOR: simba.hjf
 * DESC:    心跳发送线程
 **/
public class HeartbeatHelper {
    private Logger logger = LoggerFactory.getLogger(HeartbeatHelper.class);
    private boolean isRunning = true;
    private int interval = 30;//second
    private Thread heartbeatThread;

    private List<String> dongfengCenterAddrList;
    private ExecutorHeartbeatInfo executorHeartbeatInfo;
    private ExecutorServiceFacade executorServiceFacade;

    public HeartbeatHelper(List<String> dongfengCenterAddrList, ExecutorHeartbeatInfo executorHeartbeatInfo, ExecutorServiceFacade executorServiceFacade) {
        this.dongfengCenterAddrList = dongfengCenterAddrList;
        this.executorHeartbeatInfo = executorHeartbeatInfo;
        this.executorServiceFacade = executorServiceFacade;
    }

    public void start() {
        heartbeatThread = new Thread() {
            @Override
            public void run() {
                while (isRunning) {
                    int heartbeatSendFailCnt = 0;
                    for (String host : dongfengCenterAddrList) {
                        try {
                            executorServiceFacade.sendHeartbeat(executorHeartbeatInfo, host);
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.warn("heartbeatThread sendHeartbeat err,host:" + host, e);
                            heartbeatSendFailCnt++;
                        }
                    }
                    if (heartbeatSendFailCnt == dongfengCenterAddrList.size()) {
                        logger.error("heartbeatThread sendHeartbeat err,all center fail.");
                    }
                    try {
                        TimeUnit.SECONDS.sleep(interval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        logger.error("HeartbeatHelper#heartbeatThread InterruptedException.", e);
                        //TODO ALARM
                    }
                }
            }
        };

        heartbeatThread.setDaemon(true);
        heartbeatThread.setName("HeartbeatHelper#heartbeatThread");
        heartbeatThread.start();
    }

    public void stop() {
        isRunning = false;
        if (heartbeatThread != null && heartbeatThread.getState() != Thread.State.TERMINATED){
            // interrupt and wait
            heartbeatThread.interrupt();
            try {
                heartbeatThread.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
