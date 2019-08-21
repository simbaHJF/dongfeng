package com.simba.dongfeng.common.pojo;

import java.util.Date;

/**
 * DATE:   2019-08-14 17:14
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class ExecutorDto {
    private long id;
    private String executorName;
    private String executorIp;
    private String executorPort;
    private String executorGroup;
    private Date activeTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public Date getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Date activeTime) {
        this.activeTime = activeTime;
    }

    @Override
    public String toString() {
        return "ExecutorDto{" +
                "id=" + id +
                ", executorName='" + executorName + '\'' +
                ", executorIp='" + executorIp + '\'' +
                ", executorPort='" + executorPort + '\'' +
                ", executorGroup='" + executorGroup + '\'' +
                ", activeTime=" + activeTime +
                '}';
    }
}
