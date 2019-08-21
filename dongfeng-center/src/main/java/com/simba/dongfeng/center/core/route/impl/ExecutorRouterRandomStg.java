package com.simba.dongfeng.center.core.route.impl;

import com.simba.dongfeng.center.core.route.ExecutorRouterStg;
import com.simba.dongfeng.center.pojo.JobDto;
import com.simba.dongfeng.center.pojo.ExecutorDto;

import java.util.List;
import java.util.Random;

/**
 * DATE:   2019-08-16 14:02
 * AUTHOR: simba.hjf
 * DESC:    随机执行器节点
 **/
public class ExecutorRouterRandomStg implements ExecutorRouterStg {

    private static Random localRandom = new Random();

    @Override
    public ExecutorDto route(JobDto jobDto, List<ExecutorDto> executors) {
        return executors.get(localRandom.nextInt(executors.size()));
    }
}
