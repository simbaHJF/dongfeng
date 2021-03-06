package com.simba.dongfeng.center.service;

import com.simba.dongfeng.center.pojo.ExecutorDto;
import com.simba.dongfeng.common.pojo.ExecutorHeartbeatInfo;

/**
 * DATE:   2019-08-14 17:27
 * AUTHOR: simba.hjf
 * DESC:
 **/
public interface CenterExecutorService {

    int heartbeatHandle(ExecutorHeartbeatInfo executorHeartbeatInfo);
}
