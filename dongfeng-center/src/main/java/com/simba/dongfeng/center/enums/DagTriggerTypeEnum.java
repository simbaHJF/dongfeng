package com.simba.dongfeng.center.enums;

/**
 * DATE:   2019-08-19 11:07
 * AUTHOR: simba.hjf
 * DESC:
 **/
public enum DagTriggerTypeEnum {
    CRON(1,"cron触发"),
    MANUAL(2,"手动触发");
    private int value;
    private String desc;

    DagTriggerTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }}
