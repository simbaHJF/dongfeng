package com.simba.dongfeng.common.builder;

import com.simba.dongfeng.common.enums.RespCodeEnum;
import com.simba.dongfeng.common.pojo.RespDto;

/**
 * DATE:   2019-08-14 17:51
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class RespDtoBuilder<T> {
    private RespDto<T> respDto;

    public static RespDtoBuilder createBuilder() {
        return new RespDtoBuilder();
    }

    public RespDtoBuilder<T> succResp(){
        respDto = new RespDto<T>(RespCodeEnum.SUCC.getCode(), RespCodeEnum.SUCC.getMsg());
        return this;
    }

    public RespDtoBuilder<T> succResp(T t){
        respDto = new RespDto<T>(RespCodeEnum.SUCC.getCode(), RespCodeEnum.SUCC.getMsg(),t);
        return this;
    }

    public RespDtoBuilder<T> badReqResp(){
        respDto = new RespDto<T>(RespCodeEnum.BAD_REQUEST.getCode(), RespCodeEnum.BAD_REQUEST.getMsg());
        return this;
    }

    public RespDtoBuilder<T> serverErrResp(){
        respDto = new RespDto<T>(RespCodeEnum.INTERNAL_SERVER_ERROR.getCode(), RespCodeEnum.INTERNAL_SERVER_ERROR.getMsg());
        return this;
    }

    public RespDtoBuilder<T> serverResourceLackResp() {
        respDto = new RespDto<T>(RespCodeEnum.SERVER_RESOURCE_LACK.getCode(), RespCodeEnum.SERVER_RESOURCE_LACK.getMsg());
        return this;
    }

    public RespDtoBuilder<T> repeatedRequestResp() {
        respDto = new RespDto<T>(RespCodeEnum.REPEATED_REQ.getCode(), RespCodeEnum.REPEATED_REQ.getMsg());
        return this;
    }

    public RespDtoBuilder<T> checkSuccResp() {
        respDto = new RespDto<T>(RespCodeEnum.CHECK_SUCC.getCode(), RespCodeEnum.CHECK_SUCC.getMsg());
        return this;
    }

    public RespDtoBuilder<T> checkFailResp() {
        respDto = new RespDto<T>(RespCodeEnum.CHECK_FAIL.getCode(), RespCodeEnum.CHECK_FAIL.getMsg());
        return this;
    }


    public RespDto<T> build() {
        return this.respDto;
    }
}
