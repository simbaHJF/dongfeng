package com.simba.dongfeng.common.pojo;

import java.io.Serializable;

/**
 * DATE:   2019-08-21 14:02
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class JobInfo implements Serializable {
    private String jobName;
    private long jobTriggerLogId;
    private int shardingIdx;
    private String launchCommand;


    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public long getJobTriggerLogId() {
        return jobTriggerLogId;
    }

    public void setJobTriggerLogId(long jobTriggerLogId) {
        this.jobTriggerLogId = jobTriggerLogId;
    }

    public int getShardingIdx() {
        return shardingIdx;
    }

    public void setShardingIdx(int shardingIdx) {
        this.shardingIdx = shardingIdx;
    }

    public String getLaunchCommand() {
        return launchCommand;
    }

    public void setLaunchCommand(String launchCommand) {
        this.launchCommand = launchCommand;
    }


    @Override
    public String toString() {
        return "JobInfo{" +
                "jobName='" + jobName + '\'' +
                ", jobTriggerLogId=" + jobTriggerLogId +
                ", shardingIdx=" + shardingIdx +
                ", launchCommand='" + launchCommand + '\'' +
                '}';
    }
}
