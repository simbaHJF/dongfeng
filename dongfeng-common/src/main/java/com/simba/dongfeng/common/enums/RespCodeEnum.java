package com.simba.dongfeng.common.enums;

/**
 * DATE:   2019-08-14 17:33
 * AUTHOR: simba.hjf
 * DESC:
 **/
public enum RespCodeEnum {
    SUCC(200,"SUCC"),
    BAD_REQUEST(400,"请求错误"),
    INTERNAL_SERVER_ERROR(500,"服务异常");
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
