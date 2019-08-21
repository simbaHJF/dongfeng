package com.simba.dongfeng.common.pojo;

import java.util.Date;

/**
 * DATE:   2019-08-19 11:02
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class DagTriggerLogDto {
    private long id;
    private long dagId;
    private int triggerType;
    private Date startTime;
    private Date endTime;
    private int status;
    private String param;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDagId() {
        return dagId;
    }

    public void setDagId(long dagId) {
        this.dagId = dagId;
    }

    public int getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(int triggerType) {
        this.triggerType = triggerType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "DagTriggerLogDto{" +
                "id=" + id +
                ", dagId=" + dagId +
                ", triggerType=" + triggerType +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status=" + status +
                ", param='" + param + '\'' +
                '}';
    }
}
