package com.simba.dongfeng.executor.pojo;

import java.util.Date;

/**
 * DATE:   2019/9/6 15:55
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class JobRecord {
    private long jobLogId;
    private int jobPid;
    private Date endTime;

    public long getJobLogId() {
        return jobLogId;
    }

    public void setJobLogId(long jobLogId) {
        this.jobLogId = jobLogId;
    }

    public int getJobPid() {
        return jobPid;
    }

    public void setJobPid(int jobPid) {
        this.jobPid = jobPid;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "JobRecord{" +
                "jobLogId=" + jobLogId +
                ", jobPid=" + jobPid +
                ", endTime=" + endTime +
                '}';
    }
}
