package com.simba.dongfeng.common.enums;

/**
 * DATE:   2019-08-14 17:33
 * AUTHOR: simba.hjf
 * DESC:
 **/
public enum RespCodeEnum {
    SUCC(200,"SUCC"),
    BAD_REQUEST(400,"请求错误"),
    INTERNAL_SERVER_ERROR(500,"服务异常"),
    SERVER_RESOURCE_LACK(600, "服务资源不足"),
    REPEATED_REQ(700,"重复请求"),
    CHECK_SUCC(800,"任务回查,任务已添加到本节点"),
    CHECK_FAIL(801,"任务回查,任务未添加到本节点");
    private int code;
    private String msg;

    RespCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
