package com.simba.dongfeng.executor.core;

import com.simba.dongfeng.common.pojo.ExecutorHeartbeatInfo;
import com.simba.dongfeng.common.pojo.JobInfo;
import com.simba.dongfeng.executor.cfg.ExecutorCfg;
import com.simba.dongfeng.executor.thread.HeartbeatHelper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

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

    private JobQueue jobQueue = new JobQueue();

    private List<String> dongfengCenterAddrList;
    private ExecutorHeartbeatInfo executorHeartbeatInfo;

    private HeartbeatHelper heartbeatHelper;

    @PostConstruct
    public void init() throws Exception {
        initDongfengCenterAddrList(executorCfg);
        initExecutorHeartbeatInfo(executorCfg);
        heartbeatHelper = new HeartbeatHelper(dongfengCenterAddrList, executorHeartbeatInfo, executorServiceFacade);
        heartbeatHelper.start();
    }


    @PreDestroy
    public void destroy() {
        if (heartbeatHelper != null) {
            heartbeatHelper.stop();
        }
    }


    public void jobTrigger(JobInfo jobInfo) {
        jobQueue.addTailIfNotEnqueue(jobInfo);
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
