package com.simba.dongfeng.center.thread;

import com.simba.dongfeng.center.core.DagQueue;
import com.simba.dongfeng.center.core.ScheduleServiceFacade;
import com.simba.dongfeng.common.pojo.DagDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * DATE:   2019-08-15 16:11
 * AUTHOR: simba.hjf
 * DESC:    dag拉取线程
 **/
public class DagFetchHelper {
    private Logger logger = LoggerFactory.getLogger(DagFetchHelper.class);

    private Thread dagFetchThread;

    private boolean isRunning = true;

    private long interval = 30;

    private DagQueue dagQueue;

    private ScheduleServiceFacade scheduleServiceFacade;


    public DagFetchHelper(DagQueue dagQueue, ScheduleServiceFacade scheduleServiceFacade) {
        this.dagQueue = dagQueue;
        this.scheduleServiceFacade = scheduleServiceFacade;
    }

    public void start() {
        dagFetchThread = new Thread(){
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        List<DagDto> dagDtoList = Optional.ofNullable(scheduleServiceFacade.fetchNeedTriggerDag()).orElse(new ArrayList<>());
                        DagDto lastDag = dagQueue.peekTail();
                        if (lastDag == null) {
                            dagQueue.addAllToTail(dagDtoList);
                        } else {
                            for (DagDto dagDto : dagDtoList) {
                                if (dagDto.getTriggerTime().compareTo(lastDag.getTriggerTime()) < 0) {
                                    continue;
                                }
                                dagQueue.addTail(dagDto);
                            }
                        }
                        TimeUnit.SECONDS.sleep(interval);
                    }catch (InterruptedException e) {
                        logger.error(e.getMessage(),e);
                        //TODO  ALARM
                    } catch (Exception e) {
                        logger.error("DagFetchHelper#dagFetchThread error.", e);
                        //TODO  ALARM
                    }
                }
            }
        };
        dagFetchThread.setDaemon(true);
        dagFetchThread.setName("DagFetchHelper#dagFetchThread");
        dagFetchThread.start();
    }

    public void stop() {
        isRunning = false;
        if (dagFetchThread != null && dagFetchThread.getState() != Thread.State.TERMINATED){
            // interrupt and wait
            dagFetchThread.interrupt();
            try {
                dagFetchThread.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
