package com.simba.dongfeng.common.pojo;

/**
 * DATE:   2019-08-19 20:42
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class CallbackDto {
    private long jobTriggerLogId;
    private int jobExecRs;
    private int shardingIdx;

    public long getJobTriggerLogId() {
        return jobTriggerLogId;
    }

    public void setJobTriggerLogId(long jobTriggerLogId) {
        this.jobTriggerLogId = jobTriggerLogId;
    }

    public int getJobExecRs() {
        return jobExecRs;
    }

    public void setJobExecRs(int jobExecRs) {
        this.jobExecRs = jobExecRs;
    }

    public int getShardingIdx() {
        return shardingIdx;
    }

    public void setShardingIdx(int shardingIdx) {
        this.shardingIdx = shardingIdx;
    }

    @Override
    public String toString() {
        return "CallbackDto{" +
                "jobTriggerLogId=" + jobTriggerLogId +
                ", jobExecRs=" + jobExecRs +
                ", shardingIdx=" + shardingIdx +
                '}';
    }
}
