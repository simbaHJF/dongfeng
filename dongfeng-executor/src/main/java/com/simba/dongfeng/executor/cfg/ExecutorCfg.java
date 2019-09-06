package com.simba.dongfeng.executor.cfg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * DATE:   2019-08-22 10:04
 * AUTHOR: simba.hjf
 * DESC:
 **/
@Component
public class ExecutorCfg {

    @Value("${dongfeng.center.addr}")
    private String dongfengCenterAddr;
    @Value("${dongfeng.executor.jar.repo.path}")
    private String jarRepoPath;
    @Value("${dongfeng.executor.sh.repo.path}")
    private String shRepoPath;
    @Value("${dongfeng.executor.name}")
    private String executorName;
    @Value("${server.port}")
    private String executorPort;
    @Value("${dongfeng.executor.group}")
    private String group;

    public String getDongfengCenterAddr() {
        return dongfengCenterAddr;
    }

    public String getJarRepoPath() {
        return jarRepoPath;
    }

    public String getShRepoPath() {
        return shRepoPath;
    }

    public String getExecutorName() {
        return executorName;
    }

    public String getExecutorPort() {
        return executorPort;
    }

    public String getGroup() {
        return group;
    }
}
