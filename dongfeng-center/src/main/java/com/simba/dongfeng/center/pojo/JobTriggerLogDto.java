package com.simba.dongfeng.center.pojo;

import java.util.Date;

/**
 * DATE:   2019-08-19 13:49
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class JobTriggerLogDto {
    private long id;
    private long jobId;
    private long dagId;
    private long dagTriggerId;
    private Date startTime;
    private Date endTime;
    private int status;
    private String centerIp;
    private String executorIp;
    private int shardingIdx;
    private int shardingCnt;
    private String param;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public long getDagId() {
        return dagId;
    }

    public void setDagId(long dagId) {
        this.dagId = dagId;
    }

    public long getDagTriggerId() {
        return dagTriggerId;
    }

    public void setDagTriggerId(long dagTriggerId) {
        this.dagTriggerId = dagTriggerId;
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

    public String getCenterIp() {
        return centerIp;
    }

    public void setCenterIp(String centerIp) {
        this.centerIp = centerIp;
    }

    public String getExecutorIp() {
        return executorIp;
    }

    public void setExecutorIp(String executorIp) {
        this.executorIp = executorIp;
    }

    public int getShardingIdx() {
        return shardingIdx;
    }

    public void setShardingIdx(int shardingIdx) {
        this.shardingIdx = shardingIdx;
    }

    public int getShardingCnt() {
        return shardingCnt;
    }

    public void setShardingCnt(int shardingCnt) {
        this.shardingCnt = shardingCnt;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }


    @Override
    public String toString() {
        return "JobTriggerLogDto{" +
                "id=" + id +
                ", jobId=" + jobId +
                ", dagId=" + dagId +
                ", dagTriggerId=" + dagTriggerId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status=" + status +
                ", centerIp='" + centerIp + '\'' +
                ", executorIp='" + executorIp + '\'' +
                ", shardingIdx=" + shardingIdx +
                ", shardingCnt=" + shardingCnt +
                ", param='" + param + '\'' +
                '}';
    }
}
