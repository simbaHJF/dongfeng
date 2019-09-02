package com.simba.dongfeng.center.enums;

/**
 * DATE:   2019-08-19 11:14
 * AUTHOR: simba.hjf
 * DESC:
 **/
public enum DagExecStatusEnum {
    RUNNINT(1,"运行中"),
    SUCC(2,"成功"),
    FAIL(3,"失败");
    private int value;
    private String desc;

    DagExecStatusEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
