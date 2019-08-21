package com.simba.dongfeng.executor.core;

import com.simba.dongfeng.common.pojo.ExecutorHeartbeatInfo;
import com.simba.dongfeng.common.util.HttpClient;
import org.springframework.stereotype.Service;

/**
 * DATE:   2019-08-21 14:48
 * AUTHOR: simba.hjf
 * DESC:    执行器节点服务方法门面
 **/
@Service
public class ExecutorServiceFacade {
    public void sendHeartbeat(ExecutorHeartbeatInfo executorHeartbeatInfo, String host) {
        HttpClient.sendPost(host,"/executor/heartbeat",executorHeartbeatInfo,3000);
    }
}
