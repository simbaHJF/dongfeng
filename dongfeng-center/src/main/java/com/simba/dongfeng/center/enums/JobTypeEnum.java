package com.simba.dongfeng.center.enums;

import java.util.stream.Stream;

/**
 * DATE:   2019-08-16 14:37
 * AUTHOR: simba.hjf
 * DESC:
 **/
public enum JobTypeEnum {
    START_NODE("start_node", 1),
    TASK_NODE("task_node", 2),
    END_NODE("end_node", 3);


    private String name;
    private int value;

    JobTypeEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public static JobTypeEnum getJobTypeEnumByValue(int value) {
        return Stream.of(JobTypeEnum.values())
                .filter(ele -> ele.getValue() == value)
                .findFirst()
                .orElse(null);
    }
}
