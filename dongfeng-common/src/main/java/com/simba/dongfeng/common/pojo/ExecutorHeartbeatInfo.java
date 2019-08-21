package com.simba.dongfeng.common.pojo;

import java.io.Serializable;

/**
 * DATE:   2019-08-21 15:01
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class ExecutorHeartbeatInfo implements Serializable {
    private String executorName;
    private String executorIp;
    private String executorPort;
    private String executorGroup;


    public String getExecutorName() {
        return executorName;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

    public String getExecutorIp() {
        return executorIp;
    }

    public void setExecutorIp(String executorIp) {
        this.executorIp = executorIp;
    }

    public String getExecutorPort() {
        return executorPort;
    }

    public void setExecutorPort(String executorPort) {
        this.executorPort = executorPort;
    }

    public String getExecutorGroup() {
        return executorGroup;
    }

    public void setExecutorGroup(String executorGroup) {
        this.executorGroup = executorGroup;
    }

    @Override
    public String toString() {
        return "ExecutorHeartbeatInfo{" +
                "executorName='" + executorName + '\'' +
                ", executorIp='" + executorIp + '\'' +
                ", executorPort='" + executorPort + '\'' +
                ", executorGroup='" + executorGroup + '\'' +
                '}';
    }
}
