package com.simba.dongfeng.center.core;

import com.simba.dongfeng.center.dao.ExecutorDao;
import com.simba.dongfeng.center.dao.JobTriggerLogDao;
import com.simba.dongfeng.center.enums.DagExecStatusEnum;
import com.simba.dongfeng.center.enums.DagTriggerTypeEnum;
import com.simba.dongfeng.center.pojo.DagDto;
import com.simba.dongfeng.center.pojo.DagTriggerLogDto;
import com.simba.dongfeng.center.pojo.ExecutorDto;
import com.simba.dongfeng.center.pojo.JobTriggerLogDto;
import com.simba.dongfeng.center.thread.CallbackHandleHelper;
import com.simba.dongfeng.center.thread.DagFetchHelper;
import com.simba.dongfeng.center.thread.DagScheduleHelper;
import com.simba.dongfeng.center.thread.ExpireLogClearHelper;
import com.simba.dongfeng.common.enums.JobStatusEnum;
import com.simba.dongfeng.common.enums.RespCodeEnum;
import com.simba.dongfeng.common.pojo.Callback;
import com.simba.dongfeng.common.pojo.RespDto;
import com.simba.dongfeng.common.util.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.List;

/**
 * DATE:   2019-08-15 15:50
 * AUTHOR: simba.hjf
 * DESC:    调度中心
 **/
@Service
public class ScheduleCenter {
    private Logger logger = LoggerFactory.getLogger(ScheduleCenter.class);

    private DagQueue dagQueue = new DagQueue();
    private CallbackQueue callbackQueue = new CallbackQueue();

    private DagFetchHelper dagFetchHelper;
    private DagScheduleHelper dagScheduleHelper;
    private CallbackHandleHelper callbackHandleHelper;
    private ExpireLogClearHelper expireLogClearHelper;


    @Resource
    private ScheduleServiceFacade scheduleServiceFacade;
    @Resource
    private JobTriggerLogDao jobTriggerLogDao;
    @Resource
    private ExecutorDao executorDao;


    @PostConstruct
    public void init() {
        dagFetchHelper = new DagFetchHelper(dagQueue, scheduleServiceFacade);
        dagScheduleHelper = new DagScheduleHelper(dagQueue, scheduleServiceFacade);
        callbackHandleHelper = new CallbackHandleHelper(callbackQueue, scheduleServiceFacade);
        expireLogClearHelper = new ExpireLogClearHelper(scheduleServiceFacade);
        dagFetchHelper.start();
        dagScheduleHelper.start();
        callbackHandleHelper.start();
        expireLogClearHelper.start();
    }

    @PreDestroy
    public void destroy() {
        if (dagFetchHelper != null) {
            dagFetchHelper.stop();
        }
        if (dagScheduleHelper != null) {
            dagScheduleHelper.stop();
        }
        if (callbackHandleHelper != null) {
            callbackHandleHelper.stop();
        }
        if (expireLogClearHelper != null) {
            expireLogClearHelper.stop();
        }
    }

    public void callback(Callback callBack) {
        callbackQueue.addTail(callBack);
    }

    public void manualTrigger(long dagId, String param) {
        DagDto dag = scheduleServiceFacade.selectDagById(dagId);
        dag.setParam(param);
        dag.setTriggerType(DagTriggerTypeEnum.MANUAL.getValue());
        dagQueue.addHead(dag);
    }


    /**
     * 中断dagLogId下的所有状态为1的job
     * 只发送中断请求,尝试中断,不保证绝对中断结果.
     * 这样做的原因是,涉及多个任务的时候,由于网络原因和其他问题,想提供一个中断时间点的逻辑统一视图太复杂了
     * 而且部分中断请求成功而部分失败的case下,中断结果的定义不好界定.所以这里只做中断尝试,不保证结果.
     * 中断结果会在executor收到中断请求后,中断对应进程,然后以回调形式通知回center来体现.
     * @param dagLogId
     */
    public void manualInterrupt(long dagLogId) {
        DagTriggerLogDto dagTriggerLogDto = scheduleServiceFacade.selectDagTriggerLogById(dagLogId);
        if (dagTriggerLogDto.getStatus() != DagExecStatusEnum.RUNNINT.getValue()) {
            return;
        }
        List<JobTriggerLogDto> jobLogs = jobTriggerLogDao.selectJobLogs(dagLogId);
        for (JobTriggerLogDto jobLog : jobLogs) {
            if (jobLog.getStatus() == JobStatusEnum.RUNNING.getValue()) {
                String host = null;
                try {
                    ExecutorDto executorDto = executorDao.selectExecutorByIp(jobLog.getExecutorIp());
                    String port = executorDto != null ? executorDto.getExecutorPort() : "8080";
                    host = jobLog.getExecutorIp() + ":" + port;
                    RespDto respDto = HttpClient.sendPost(host, "/dongfengexecutor/job/interrupt", jobLog.getId(), 300000);
                    if (respDto.getCode() != RespCodeEnum.SUCC.getCode()) {
                        logger.error("manualInterrupt request fail.jobId:" + jobLog.getId() + "host:" + host);
                    }
                } catch (Exception e) {
                    logger.error("manualInterrupt request err.jobId:" + jobLog.getId() + "host:" + host, e);
                }
            }
        }

    }

}
