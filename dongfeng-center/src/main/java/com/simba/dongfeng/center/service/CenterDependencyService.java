package com.simba.dongfeng.center.service;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * DATE:   2019/8/29 20:56
 * AUTHOR: simba.hjf
 * DESC:
 **/
public interface CenterDependencyService {
    List<Long> selectParentJobIdList(long jobId);
}
