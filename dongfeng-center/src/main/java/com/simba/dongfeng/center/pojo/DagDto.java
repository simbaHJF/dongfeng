package com.simba.dongfeng.center.pojo;

import java.util.Date;

/**
 * DATE:   2019-08-15 16:30
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class DagDto {
    private long id;
    private String dagName;
    private String dagGroup;
    private String dagCron;
    private int status;
    private Date triggerTime;
    private String param;
    private String alarm;

    //非数据库字段,只在调度和手动触发时,区分队列中的dagDto的触发方式
    private int triggerType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDagName() {
        return dagName;
    }

    public void setDagName(String dagName) {
        this.dagName = dagName;
    }

    public String getDagGroup() {
        return dagGroup;
    }

    public void setDagGroup(String dagGroup) {
        this.dagGroup = dagGroup;
    }

    public String getDagCron() {
        return dagCron;
    }

    public void setDagCron(String dagCron) {
        this.dagCron = dagCron;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(Date triggerTime) {
        this.triggerTime = triggerTime;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public int getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(int triggerType) {
        this.triggerType = triggerType;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    @Override
    public String toString() {
        return "DagDto{" +
                "id=" + id +
                ", dagName='" + dagName + '\'' +
                ", dagGroup='" + dagGroup + '\'' +
                ", dagCron='" + dagCron + '\'' +
                ", status=" + status +
                ", triggerTime=" + triggerTime +
                ", param='" + param + '\'' +
                ", alarm='" + alarm + '\'' +
                ", triggerType=" + triggerType +
                '}';
    }
}
