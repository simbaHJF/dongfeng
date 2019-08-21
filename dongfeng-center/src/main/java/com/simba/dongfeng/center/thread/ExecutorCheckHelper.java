package com.simba.dongfeng.center.thread;

import com.simba.dongfeng.center.dao.ExecutorDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * DATE:   2019-08-14 18:23
 * AUTHOR: simba.hjf
 * DESC:    执行器健康监测线程
 **/
public class ExecutorCheckHelper {

    private Logger logger = LoggerFactory.getLogger(ExecutorCheckHelper.class);

    private ExecutorDao executorDao;
    private boolean isRunning = true;
    private long interval = 30;
    private Thread checkThread;

    public ExecutorCheckHelper(ExecutorDao executorDao) {
        this.executorDao = executorDao;
    }

    public void start() {
        checkThread = new Thread() {
            @Override
            public void run() {
                while (isRunning) {

                    try {
                        Date deadlineTime = Timestamp.valueOf(LocalDateTime.now().minusSeconds(90));
                        executorDao.deleteExpiredExecutor(deadlineTime);
                        TimeUnit.SECONDS.sleep(interval);
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage(),e);
                    } catch (Exception e) {
                        logger.error("ExecutorCheckHelper#checkThread error.", e);
                    }
                }
            }
        };
        checkThread.setDaemon(true);
        checkThread.setName("executorCheckThread");
        checkThread.start();
    }

    public void stop() {
        isRunning = false;
        if (checkThread != null && checkThread.getState() != Thread.State.TERMINATED){
            checkThread.interrupt();
            try {
                checkThread.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

}
