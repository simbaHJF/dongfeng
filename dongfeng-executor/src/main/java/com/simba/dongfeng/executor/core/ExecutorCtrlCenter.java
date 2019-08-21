package com.simba.dongfeng.executor.core;

import com.simba.dongfeng.common.pojo.ExecutorHeartbeatInfo;
import com.simba.dongfeng.executor.thread.HeartbeatHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * DATE:   2019-08-21 14:25
 * AUTHOR: simba.hjf
 * DESC:
 **/
@Service
public class ExecutorCtrlCenter {
    @Value("dongfeng.center.addr")
    private String dongfengCenterAddr;
    @Value("dongfeng.executor.jar.repo.path")
    private String jarRepoPath;
    @Value("dongfeng.executor.sh.repo.path")
    private String shRepoPath;
    @Value("dongfeng.executor.name")
    private String executorName;
    @Value("dongfeng.executor.port")
    private String executorPort;
    @Value("dongfeng.executor.group")
    private String group;

    @Resource
    private ExecutorServiceFacade executorServiceFacade;

    private List<String> dongfengCenterAddrList = new ArrayList<>();
    private ExecutorHeartbeatInfo executorHeartbeatInfo;

    private HeartbeatHelper heartbeatHelper;

    @PostConstruct
    public void init() throws Exception {
        initDongfengCenterAddrList();
        initExecutorHeartbeatInfo();
        heartbeatHelper = new HeartbeatHelper(dongfengCenterAddrList, executorHeartbeatInfo, executorServiceFacade);
        heartbeatHelper.start();
    }


    @PreDestroy
    public void destroy() {
        if (heartbeatHelper != null) {
            heartbeatHelper.stop();
        }
    }

    private void initDongfengCenterAddrList() {
        String[] addrs = dongfengCenterAddr.split(",");
        for (String addr : addrs) {
            dongfengCenterAddrList.add(addr);
        }
    }

    private void initExecutorHeartbeatInfo() throws Exception{
        executorHeartbeatInfo = new ExecutorHeartbeatInfo();
        executorHeartbeatInfo.setExecutorName(executorName);
        executorHeartbeatInfo.setExecutorPort(executorPort);
        executorHeartbeatInfo.setExecutorGroup(group);
        executorHeartbeatInfo.setExecutorIp(InetAddress.getLocalHost().getHostAddress());
    }
}
