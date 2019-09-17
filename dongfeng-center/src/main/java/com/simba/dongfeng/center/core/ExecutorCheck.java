package com.simba.dongfeng.center.core;

import com.simba.dongfeng.center.dao.ExecutorDao;
import com.simba.dongfeng.center.thread.ExecutorCheckHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * DATE:   2019-08-15 15:37
 * AUTHOR: simba.hjf
 * DESC:   执行器节点健康检测服务
 **/
@Service
public class ExecutorCheck {
    private Logger logger = LoggerFactory.getLogger(ExecutorCheck.class);

    private ExecutorCheckHelper executorCheckHelper;


    @Resource
    private ExecutorDao executorDao;

    @PostConstruct
    public void init() {
        executorCheckHelper = new ExecutorCheckHelper(executorDao);
        //executorCheckHelper.start();
    }

    @PreDestroy
    public void destroy() {
        if (executorCheckHelper != null) {
            executorCheckHelper.stop();
        }
    }
}
