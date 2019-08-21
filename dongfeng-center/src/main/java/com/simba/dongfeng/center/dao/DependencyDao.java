package com.simba.dongfeng.center.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * DATE:   2019-08-16 16:33
 * AUTHOR: simba.hjf
 * DESC:
 **/
@Mapper
public interface DependencyDao {

    @Select("select job_id from dependency where parent_job_id = #{parentId}")
    List<Long> selectChildJobIdList(@Param("parentId") long parentId);

    @Select("select parent_job_id from dependency where job_id = #{jobId}")
    List<Long> selectParentJobIdList(@Param("jobId") long jobId);
}
