package com.simba.dongfeng.executor.pojo;

import com.simba.dongfeng.common.pojo.JobInfo;

import java.util.Date;

/**
 * DATE:   2019/9/6 15:55
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class JobRecord {
    private Process process;
    private Date endTime;
    private JobInfo jobInfo;


    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public JobInfo getJobInfo() {
        return jobInfo;
    }

    public void setJobInfo(JobInfo jobInfo) {
        this.jobInfo = jobInfo;
    }

    @Override
    public String toString() {
        return "JobRecord{" +
                ", process=" + process +
                ", endTime=" + endTime +
                ", jobInfo=" + jobInfo +
                '}';
    }
}
