package com.simba.dongfeng.center.service;

import com.simba.dongfeng.center.pojo.JobDto;

import java.util.List;

/**
 * DATE:   2019/8/29 20:36
 * AUTHOR: simba.hjf
 * DESC:
 **/
public interface CenterJobService {
    List<JobDto> selectAllJob();
}
