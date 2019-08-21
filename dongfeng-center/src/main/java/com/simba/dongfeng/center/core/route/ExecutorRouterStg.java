package com.simba.dongfeng.center.core.route;

import com.simba.dongfeng.center.pojo.JobDto;
import com.simba.dongfeng.center.pojo.ExecutorDto;

import java.util.List;

/**
 * DATE:   2019-08-16 13:59
 * AUTHOR: simba.hjf
 * DESC:    执行器路由策略接口
 **/
public interface ExecutorRouterStg {

    /**
     * 根据job信息和执行器列表进行执行器路由
     * @param jobDto
     * @param executors
     * @return
     */
    ExecutorDto route(JobDto jobDto, List<ExecutorDto> executors);
}
