package com.simba.dongfeng.common.pojo;

import java.io.Serializable;

/**
 * DATE:   2019-08-14 17:32
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class RespDto<T> implements Serializable {
    int code;
    String msg;
    T respDto;


    public RespDto(int code, String msg, T respDto) {
        this.code = code;
        this.msg = msg;
        this.respDto = respDto;
    }

    public RespDto(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getRespDto() {
        return respDto;
    }

    public void setRespDto(T respDto) {
        this.respDto = respDto;
    }

    @Override
    public String toString() {
        return "RespDto{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", respDto=" + respDto +
                '}';
    }
}
