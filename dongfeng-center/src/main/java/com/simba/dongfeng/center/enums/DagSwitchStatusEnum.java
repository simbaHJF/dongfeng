package com.simba.dongfeng.center.enums;

/**
 * DATE:   2019/9/2 20:28
 * AUTHOR: simba.hjf
 * DESC:
 **/
public enum DagSwitchStatusEnum {
    OFF(1,"关闭"),
    ON(2,"开启");
    private int value;
    private String desc;

    DagSwitchStatusEnum(int value, String desc) {
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
