package com.simba.dongfeng.center.converter;

import com.simba.dongfeng.center.pojo.ExecutorDto;
import com.simba.dongfeng.common.pojo.ExecutorHeartbeatInfo;

import java.util.Date;

/**
 * DATE:   2019-08-21 15:38
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class PojoConverter {
    public static ExecutorDto convertExecutorHeartbeatInfo(ExecutorHeartbeatInfo executorHeartbeatInfo) {
        ExecutorDto executorDto = new ExecutorDto();
        executorDto.setExecutorName(executorHeartbeatInfo.getExecutorName());
        executorDto.setExecutorIp(executorHeartbeatInfo.getExecutorIp());
        executorDto.setExecutorPort(executorHeartbeatInfo.getExecutorPort());
        executorDto.setExecutorGroup(executorHeartbeatInfo.getExecutorGroup());
        executorDto.setActiveTime(new Date());

        return executorDto;
    }
}
