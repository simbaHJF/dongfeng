package com.simba.dongfeng.center.dao;

import com.simba.dongfeng.center.pojo.DependencyDto;
import org.apache.ibatis.annotations.*;
import sun.rmi.runtime.Log;

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

    @Insert("insert into dependency(job_id,parent_job_id,dag_id) " +
            "values(#{dependency.jobId},#{dependency.parentJobId},#{dependency.dagId})")
    int insertDependency(@Param("dependency") DependencyDto dependency);

    @Delete("delete from dependency where dag_id = #{dagId}")
    int deleteDependencyByDagId(@Param("dagId") long dagId);

    @Delete("delete from dependency where job_id = #{jobId}")
    int deleteDependencyByJobId(@Param("jobId") long jobId);
}
