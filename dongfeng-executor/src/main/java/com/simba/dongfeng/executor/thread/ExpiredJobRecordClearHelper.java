package com.simba.dongfeng.executor.thread;

import com.simba.dongfeng.executor.pojo.JobRecordPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * DATE:   2019/9/6 16:28
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class ExpiredJobRecordClearHelper {
    private Logger logger = LoggerFactory.getLogger(HeartbeatHelper.class);
    private boolean isRunning = true;
    private int interval = 1;//hour
    private Thread expiredJobRecordClearThread;
    private JobRecordPool jobRecordPool;

    public ExpiredJobRecordClearHelper(JobRecordPool jobRecordPool) {
        this.jobRecordPool = jobRecordPool;
    }

    public void start() {
        expiredJobRecordClearThread = new Thread() {
            @Override
            public void run() {
                while (isRunning) {
                    jobRecordPool.deleteExpireJobRecord();
                    try {
                        TimeUnit.HOURS.sleep(interval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        logger.error("ExpiredJobRecordClearHelper#expiredJobRecordClearThread InterruptedException.", e);
                    }
                }
            }
        };
        expiredJobRecordClearThread.setDaemon(true);
        expiredJobRecordClearThread.setName("ExpiredJobRecordClearHelper#expiredJobRecordClearThread");
        expiredJobRecordClearThread.start();
    }

    public void stop() {
        isRunning = false;
        if (expiredJobRecordClearThread != null && expiredJobRecordClearThread.getState() != Thread.State.TERMINATED){
            // interrupt and wait
            expiredJobRecordClearThread.interrupt();
            try {
                expiredJobRecordClearThread.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

}
