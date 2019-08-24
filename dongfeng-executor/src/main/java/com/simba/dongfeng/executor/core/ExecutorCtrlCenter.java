package com.simba.dongfeng.executor.core;

import com.simba.dongfeng.common.pojo.ExecutorHeartbeatInfo;
import com.simba.dongfeng.common.pojo.JobInfo;
import com.simba.dongfeng.executor.cfg.ExecutorCfg;
import com.simba.dongfeng.executor.thread.CallbackNotifyHelper;
import com.simba.dongfeng.executor.thread.HeartbeatHelper;
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

    private CallbackQueue callbackQueue = new CallbackQueue();
    private ExecutorService threadPoolExecutor = new ThreadPoolExecutor(3, 3, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(2));

    private List<String> dongfengCenterAddrList;
    private ExecutorHeartbeatInfo executorHeartbeatInfo;

    private HeartbeatHelper heartbeatHelper;
    private CallbackNotifyHelper callbackNotifyHelper;

    @PostConstruct
    public void init() throws Exception {
        initDongfengCenterAddrList(executorCfg);
        initExecutorHeartbeatInfo(executorCfg);
        heartbeatHelper = new HeartbeatHelper(dongfengCenterAddrList, executorHeartbeatInfo, executorServiceFacade);
        heartbeatHelper.start();
        callbackNotifyHelper = new CallbackNotifyHelper(callbackQueue, dongfengCenterAddrList);
        callbackNotifyHelper.start();
    }


    @PreDestroy
    public void destroy() {
        if (heartbeatHelper != null) {
            heartbeatHelper.stop();
        }
        if (callbackNotifyHelper != null) {
            callbackNotifyHelper.stop();
        }
    }


    public void jobTrigger(JobInfo jobInfo) {
        WorkWarpper workWarpper = new WorkWarpper(jobInfo, callbackQueue);
        threadPoolExecutor.execute(workWarpper);
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
