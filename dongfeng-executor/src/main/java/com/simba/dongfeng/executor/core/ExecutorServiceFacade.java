package com.simba.dongfeng.executor.core;

import com.simba.dongfeng.common.pojo.ExecutorHeartbeatInfo;
import com.simba.dongfeng.common.pojo.RespDto;
import com.simba.dongfeng.common.util.HttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * DATE:   2019-08-21 14:48
 * AUTHOR: simba.hjf
 * DESC:    执行器节点服务方法门面
 **/
@Service
public class ExecutorServiceFacade {
    private Logger logger = LoggerFactory.getLogger(ExecutorServiceFacade.class);

    public void sendHeartbeat(ExecutorHeartbeatInfo executorHeartbeatInfo, String host) {
        logger.info("send executorHeartbeatInfo:" + executorHeartbeatInfo);

        HttpClient.sendPost(host,"/dongfeng/executor/heartbeat",executorHeartbeatInfo,5000);

    }
}
