package com.simba.dongfeng.center.pojo;

/**
 * DATE:   2019-08-16 16:34
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class DependencyDto {
    private long id;
    private long jobId;
    private long parentJobId;
    private long dagId;

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

    public long getParentJobId() {
        return parentJobId;
    }

    public void setParentJobId(long parentJobId) {
        this.parentJobId = parentJobId;
    }

    public long getDagId() {
        return dagId;
    }

    public void setDagId(long dagId) {
        this.dagId = dagId;
    }

    @Override
    public String toString() {
        return "DependencyDto{" +
                "id=" + id +
                ", jobId=" + jobId +
                ", parentJobId=" + parentJobId +
                ", dagId=" + dagId +
                '}';
    }
}
