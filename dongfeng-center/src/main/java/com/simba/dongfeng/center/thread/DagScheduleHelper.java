package com.simba.dongfeng.center.thread;

import com.simba.dongfeng.center.core.DagQueue;
import com.simba.dongfeng.center.core.ScheduleServiceFacade;
import com.simba.dongfeng.center.enums.DagExecStatusEnum;
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
                    DagDto dagDto = null;
                    try {
                        dagDto = dagQueue.takeHead();
                        logger.info("dagScheduleThread ## dagQueue head dag:" + dagDto);
                        if (dagDto.getTriggerType() == DagTriggerTypeEnum.CRON.getValue()) {
                            Date now = new Date();
                            if (now.before(dagDto.getTriggerTime())) {
                                TimeUnit.MILLISECONDS.sleep(dagDto.getTriggerTime().getTime() - now.getTime());
                            }
                        }
                        logger.info("testA##########");
                        // 写dag调度日志
                        DagTriggerLogDto dagTriggerLogDto = generateDagTriggerLogDto(dagDto.getId(), dagDto.getDagName(), dagDto.getParam(), dagDto.getTriggerType());
                        scheduleServiceFacade.insertDagTriggerLog(dagTriggerLogDto);
                        logger.info("testB##########");
                        // 写start job触发日志
                        JobDto startJob = scheduleServiceFacade.selectStartJobWithDagId(dagDto);
                        logger.info("testC##########");
                        InetAddress addr = InetAddress.getLocalHost();
                        logger.info("testD##########");
                        JobTriggerLogDto startJobTriggerLog = scheduleServiceFacade.generateJobTriggerLogDto(startJob.getId(),startJob.getJobName(), dagDto.getId(), dagTriggerLogDto.getId(), JobStatusEnum.SUCC.getValue(), addr.getHostAddress(), addr.getHostAddress(), dagDto.getParam());
                        logger.info("testE###########");
                        startJobTriggerLog.setEndTime(new Date());
                        scheduleServiceFacade.insertJobTriggerLog(startJobTriggerLog);
                        logger.info("testF##########");
                        //  获取所有子任务,进行调度
                        List<JobDto> childJobList = Optional.ofNullable(scheduleServiceFacade.selectChildJobList(startJob.getId())).orElse(new ArrayList<>());
                        for (JobDto jobDto : childJobList) {
                            scheduleServiceFacade.scheduleJob(jobDto, dagTriggerLogDto, 2, addr.getHostAddress());
                        }

                    } catch (InterruptedException e) {
                        logger.error("DagScheduleHelper ## dagScheduleThread InterruptedException.dag:" + dagDto, e);
                        e.printStackTrace();
                        //TODO alarm
                    } catch (Exception e) {
                        logger.error("DagScheduleHelper ## dagScheduleThread err.dag:" + dagDto, e);
                        e.printStackTrace();
                        //TODO alarm
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


    private DagTriggerLogDto generateDagTriggerLogDto(long dagId, String dagName, String param, int dagTriggerType) {
        DagTriggerLogDto dagTriggerLogDto = new DagTriggerLogDto();
        dagTriggerLogDto.setDagId(dagId);
        dagTriggerLogDto.setDagName(dagName);
        dagTriggerLogDto.setStartTime(new Date());
        dagTriggerLogDto.setStatus(DagExecStatusEnum.RUNNINT.getValue());
        dagTriggerLogDto.setTriggerType(dagTriggerType);
        dagTriggerLogDto.setParam(param);
        return dagTriggerLogDto;
    }

}
