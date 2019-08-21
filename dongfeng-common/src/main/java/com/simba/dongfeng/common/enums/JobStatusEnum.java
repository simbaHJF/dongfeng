package com.simba.dongfeng.common.enums;

/**
 * DATE:   2019-08-19 14:50
 * AUTHOR: simba.hjf
 * DESC:
 **/
public enum JobStatusEnum {
    RUNNING(1, "运行中"),
    SUCC(2, "成功"),
    FAIL(3, "失败");
    private int value;
    private String desc;


    JobStatusEnum(int value, String desc) {
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
