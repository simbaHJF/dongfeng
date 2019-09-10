package com.simba.dongfeng.center.service;

import com.github.pagehelper.PageInfo;
import com.simba.dongfeng.center.pojo.DependencyDto;
import com.simba.dongfeng.center.pojo.JobDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * DATE:   2019/8/29 20:36
 * AUTHOR: simba.hjf
 * DESC:
 **/
public interface CenterJobService {
    PageInfo<JobDto> selectJobByPage(int page, int pageSize, long dagId);

    void insertJobAndDependency(JobDto jobDto);

    JobDto selectJobById(long jobId);

    int updateJob(JobDto jobDto);

    boolean deleteJob(long jobId);
}
