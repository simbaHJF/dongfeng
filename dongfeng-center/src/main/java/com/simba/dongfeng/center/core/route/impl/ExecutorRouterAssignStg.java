package com.simba.dongfeng.center.core.route.impl;

import com.simba.dongfeng.center.core.route.ExecutorRouterStg;
import com.simba.dongfeng.center.pojo.JobDto;
import com.simba.dongfeng.center.pojo.ExecutorDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DATE:   2019-08-16 14:10
 * AUTHOR: simba.hjf
 * DESC:
 **/
public class ExecutorRouterAssignStg implements ExecutorRouterStg {
    @Override
    public ExecutorDto route(JobDto jobDto, List<ExecutorDto> executors) {
        String ip = jobDto.getAssignIp();
        return Optional.ofNullable(executors)
                .orElse(new ArrayList<>())
                .stream()
                .filter(ele -> ele.getExecutorIp().equals(ip))
                .findAny()
                .orElse(null);
    }
}
