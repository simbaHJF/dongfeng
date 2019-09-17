package com.simba.dongfeng.center.thread;

import com.simba.dongfeng.center.core.ScheduleServiceFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * DATE:   2019/9/7 10:04
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class ExpireLogClearHelper {
    private Logger logger = LoggerFactory.getLogger(ExpireLogClearHelper.class);

    private boolean isRunning = true;
    private long interval = 1;//day
    private Thread expireLogClearThread;

    private ScheduleServiceFacade scheduleServiceFacade;

    public ExpireLogClearHelper(ScheduleServiceFacade scheduleServiceFacade) {
        this.scheduleServiceFacade = scheduleServiceFacade;
    }

    public void start() {
        expireLogClearThread = new Thread(){
            @Override
            public void run() {
                while (isRunning) {
                    scheduleServiceFacade.deleteExpiredDagLog();
                    scheduleServiceFacade.deleteExpiredJobLog();

                    try {
                        TimeUnit.DAYS.sleep(interval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        logger.error("ExpireLogClearHelper#expireLogClearThread InterruptedException",e);
                    } catch (Exception e) {
                        logger.error("ExpireLogClearHelper#expireLogClearThread error.", e);
                    }
                }
            }
        };

        expireLogClearThread.setName("ExpireLogClearHelper#expireLogClearThread");
        expireLogClearThread.setDaemon(true);
        expireLogClearThread.start();
    }


    public void stop() {
        isRunning = false;
        if (expireLogClearThread != null){
            expireLogClearThread.interrupt();
            try {
                expireLogClearThread.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
