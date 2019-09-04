package com.simba.dongfeng.center.thread;

import com.alibaba.fastjson.JSON;
import com.simba.dongfeng.center.core.DagQueue;
import com.simba.dongfeng.center.core.ScheduleServiceFacade;
import com.simba.dongfeng.center.pojo.DagDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    //dag预读拉取,时间窗口,秒
    private int fetchTimeWindow = 60;

    private DagQueue dagQueue;

    private ScheduleServiceFacade scheduleServiceFacade;


    public DagFetchHelper(DagQueue dagQueue, ScheduleServiceFacade scheduleServiceFacade) {
        this.dagQueue = dagQueue;
        this.scheduleServiceFacade = scheduleServiceFacade;
    }

    public void start() {
        dagFetchThread = new Thread() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        List<DagDto> dagDtoList = Optional.ofNullable(scheduleServiceFacade.fetchNeedTriggerDag(fetchTimeWindow)).orElse(new ArrayList<>());
                        logger.info("dagFetchThread ## fetch dag list:" + JSON.toJSONString(dagDtoList));
                        System.out.println("dagFetchThread ## fetch dag list:" + JSON.toJSONString(dagDtoList));
                        dagQueue.addAllToTail(dagDtoList);
                        /*DagDto lastDag = dagQueue.peekTail();
                        if (lastDag == null) {
                            dagQueue.addAllToTail(dagDtoList);
                        } else {
                            for (DagDto dagDto : dagDtoList) {
                                if (dagDto.getTriggerTime().compareTo(lastDag.getTriggerTime()) < 0) {
                                    continue;
                                }
                                dagQueue.addTail(dagDto);
                            }
                        }*/
                        TimeUnit.SECONDS.sleep(interval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        logger.error("DagFetchHelper ## dagFetchThread error.", e);
                        //TODO  ALARM
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("DagFetchHelper ## dagFetchThread error.", e);
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
        if (dagFetchThread != null) {
            // interrupt and wait
            dagFetchThread.interrupt();
            try {
                dagFetchThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.error(e.getMessage(), e);
            }
        }
    }
}
