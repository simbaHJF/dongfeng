package com.simba.dongfeng.center.thread;

import com.simba.dongfeng.center.core.DagQueue;
import com.simba.dongfeng.center.core.ScheduleServiceFacade;
import com.simba.dongfeng.center.enums.DagStatusEnum;
import com.simba.dongfeng.center.enums.DagTriggerTypeEnum;
import com.simba.dongfeng.center.pojo.DagDto;
import com.simba.dongfeng.center.pojo.DagTriggerLogDto;
import com.simba.dongfeng.center.pojo.JobDto;
import com.simba.dongfeng.center.pojo.JobTriggerLogDto;
import com.simba.dongfeng.common.enums.JobStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * DATE:   2019-08-16 11:18
 * AUTHOR: simba.hjf
 * DESC:    dag调度线程
 **/
public class DagScheduleHelper {
    private Logger logger = LoggerFactory.getLogger(DagScheduleHelper.class);

    private boolean isRunning = true;
    private Thread dagScheduleThread;


    private DagQueue dagQueue;
    private ScheduleServiceFacade scheduleServiceFacade;

    public DagScheduleHelper(DagQueue dagQueue, ScheduleServiceFacade scheduleServiceFacade) {
        this.dagQueue = dagQueue;
        this.scheduleServiceFacade = scheduleServiceFacade;
    }

    public void start() {
        dagScheduleThread = new Thread() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        DagDto dagDto = dagQueue.takeHead();
                        if (dagDto.getTriggerType() == DagTriggerTypeEnum.CRON.getValue()) {
                            Date now = new Date();
                            if (now.before(dagDto.getTriggerTime())) {
                                TimeUnit.MILLISECONDS.sleep(dagDto.getTriggerTime().getTime() - now.getTime());
                            }
                        }

                        JobDto startJob = scheduleServiceFacade.selectStartJobWithDagId(dagDto);
                        DagTriggerLogDto dagTriggerLogDto = generateDagTriggerLogDto(dagDto.getId(), dagDto.getParam(), dagDto.getTriggerType());
                        scheduleServiceFacade.insertDagTriggerLog(dagTriggerLogDto);
                        InetAddress addr = InetAddress.getLocalHost();
                        JobTriggerLogDto startJobTriggerLog = scheduleServiceFacade.generateJobTriggerLogDto(startJob.getId(), dagDto.getId(), dagTriggerLogDto.getId(), JobStatusEnum.SUCC.getValue(), addr.getHostAddress(), addr.getHostAddress(), dagDto.getParam());
                        scheduleServiceFacade.insertJobTriggerLog(startJobTriggerLog);

                        List<JobDto> childJobList = Optional.ofNullable(scheduleServiceFacade.selectChildJobList(startJob.getId())).orElse(new ArrayList<>());

                        for (JobDto jobDto : childJobList) {
                            scheduleServiceFacade.scheduleJob(jobDto, dagTriggerLogDto, 2, addr.getHostAddress());
                        }

                    } catch (InterruptedException e) {
                        //TODO alarm
                        logger.error("dagScheduleThread err.", e);
                        System.out.println("12345");
                        e.printStackTrace();
                        return;
                    } catch (Exception e) {
                        //TODO alarm
                        logger.error("dagScheduleThread err,get local ip err.", e);
                        System.out.println("54321");
                        e.printStackTrace();
                        return;
                    }
                }
            }
        };
        dagScheduleThread.setDaemon(true);
        dagScheduleThread.setName("DagScheduleHelper#dagScheduleThread");
        dagScheduleThread.start();

    }

    public void stop() {
        isRunning = false;
        if (dagScheduleThread != null) {
            // interrupt and wait
            dagScheduleThread.interrupt();
            try {
                dagScheduleThread.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }


    private DagTriggerLogDto generateDagTriggerLogDto(long dagId, String param, int dagTriggerType) {
        DagTriggerLogDto dagTriggerLogDto = new DagTriggerLogDto();
        dagTriggerLogDto.setDagId(dagId);
        dagTriggerLogDto.setStartTime(new Date());
        dagTriggerLogDto.setStatus(DagStatusEnum.RUNNINT.getValue());
        dagTriggerLogDto.setTriggerType(dagTriggerType);
        dagTriggerLogDto.setParam(param);
        return dagTriggerLogDto;
    }

}
