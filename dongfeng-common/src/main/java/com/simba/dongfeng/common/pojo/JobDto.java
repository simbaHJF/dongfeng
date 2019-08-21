package com.simba.dongfeng.common.pojo;

/**
 * DATE:   2019-08-15 16:16
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class JobDto {
    private long id;
    private String jobName;
    private int jobType;
    private int shardingCnt;
    private long dagId;
    private String scheduleType;
    private String launchCommand;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public int getJobType() {
        return jobType;
    }

    public void setJobType(int jobType) {
        this.jobType = jobType;
    }

    public int getShardingCnt() {
        return shardingCnt;
    }

    public void setShardingCnt(int shardingCnt) {
        this.shardingCnt = shardingCnt;
    }

    public long getDagId() {
        return dagId;
    }

    public void setDagId(long dagId) {
        this.dagId = dagId;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public String getLaunchCommand() {
        return launchCommand;
    }

    public void setLaunchCommand(String launchCommand) {
        this.launchCommand = launchCommand;
    }

    @Override
    public String toString() {
        return "JobDto{" +
                "id=" + id +
                ", jobName='" + jobName + '\'' +
                ", jobType=" + jobType +
                ", shardingCnt=" + shardingCnt +
                ", dagId=" + dagId +
                ", scheduleType='" + scheduleType + '\'' +
                ", launchCommand='" + launchCommand + '\'' +
                '}';
    }
}
