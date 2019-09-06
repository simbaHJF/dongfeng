package com.simba.dongfeng.executor.core;

import com.simba.dongfeng.common.pojo.ExecutorHeartbeatInfo;
import com.simba.dongfeng.common.pojo.JobInfo;
import com.simba.dongfeng.executor.cfg.ExecutorCfg;
import com.simba.dongfeng.executor.pojo.JobRecord;
import com.simba.dongfeng.executor.pojo.JobRecordPool;
import com.simba.dongfeng.executor.thread.CallbackNotifyHelper;
import com.simba.dongfeng.executor.thread.ExpiredJobRecordClearHelper;
import com.simba.dongfeng.executor.thread.HeartbeatHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * DATE:   2019-08-21 14:25
 * AUTHOR: simba.hjf
 * DESC:    执行器控制中心
 **/
@Service
public class ExecutorCtrlCenter {

    @Resource
    private ExecutorCfg executorCfg;
    @Resource
    private ExecutorServiceFacade executorServiceFacade;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private CallbackQueue callbackQueue = new CallbackQueue();
    private JobRecordPool jobRecordPool = new JobRecordPool();

    //任务执行池
    private ExecutorService threadPoolExecutor = new ThreadPoolExecutor(3, 3, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(2));


    private List<String> dongfengCenterAddrList;
    private ExecutorHeartbeatInfo executorHeartbeatInfo;

    private HeartbeatHelper heartbeatHelper;
    private CallbackNotifyHelper callbackNotifyHelper;
    private ExpiredJobRecordClearHelper expiredJobRecordClearHelper;

    @PostConstruct
    public void init() throws Exception {
        initDongfengCenterAddrList(executorCfg);
        initExecutorHeartbeatInfo(executorCfg);
        heartbeatHelper = new HeartbeatHelper(dongfengCenterAddrList, executorHeartbeatInfo, executorServiceFacade);
        heartbeatHelper.start();
        callbackNotifyHelper = new CallbackNotifyHelper(executorHeartbeatInfo.getExecutorIp(), callbackQueue, dongfengCenterAddrList);
        callbackNotifyHelper.start();
        expiredJobRecordClearHelper = new ExpiredJobRecordClearHelper(jobRecordPool);
        expiredJobRecordClearHelper.start();
    }


    @PreDestroy
    public void destroy() {
        if (heartbeatHelper != null) {
            heartbeatHelper.stop();
        }
        if (callbackNotifyHelper != null) {
            callbackNotifyHelper.stop();
        }
        if (expiredJobRecordClearHelper != null) {
            expiredJobRecordClearHelper.stop();
        }
    }


    public boolean writeJobLogIdToRedis(long jobLogId) {
        return stringRedisTemplate.opsForValue().setIfAbsent("dongfeng_schedule_" + jobLogId, executorHeartbeatInfo.getExecutorIp() + ":" + executorHeartbeatInfo.getExecutorPort(), 10, TimeUnit.MINUTES);
    }

    public boolean deleteJobLogIdKeyInRedis(long jobLogId) {
        return stringRedisTemplate.delete(String.valueOf(jobLogId));
    }


    public boolean checkJob(long jobLogId) {
        return jobRecordPool.getJobRecord(jobLogId) != null;
    }

    public void jobTrigger(JobInfo jobInfo) {
        JobRecord jobRecord = new JobRecord();
        jobRecord.setJobLogId(jobInfo.getJobTriggerLogId());
        WorkWarpper workWarpper = new WorkWarpper(jobInfo, callbackQueue,jobRecord);
        try {
            jobRecordPool.lockPool();
            jobRecordPool.putJobRecord(jobRecord);
            threadPoolExecutor.execute(workWarpper);
        } finally {
            jobRecordPool.unlockPool();
        }
    }

    private void initDongfengCenterAddrList(ExecutorCfg executorCfg) {
        dongfengCenterAddrList = new ArrayList<>();
        String[] addrs = executorCfg.getDongfengCenterAddr().split(",");
        for (String addr : addrs) {
            dongfengCenterAddrList.add(addr);
        }
    }

    private void initExecutorHeartbeatInfo(ExecutorCfg executorCfg) throws Exception {
        executorHeartbeatInfo = new ExecutorHeartbeatInfo();
        executorHeartbeatInfo.setExecutorName(executorCfg.getExecutorName());
        executorHeartbeatInfo.setExecutorPort(executorCfg.getExecutorPort());
        executorHeartbeatInfo.setExecutorGroup(executorCfg.getGroup());
        executorHeartbeatInfo.setExecutorIp(InetAddress.getLocalHost().getHostAddress());
    }
}
