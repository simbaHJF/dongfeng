package com.simba.dongfeng.center.dao;

import com.simba.dongfeng.common.pojo.JobDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * DATE:   2019-08-15 16:52
 * AUTHOR: simba.hjf
 * DESC:
 **/
@Mapper
public interface JobDao {


    @Select("select id,job_name,job_type,sharding_cnt,dag_id,schedule_type,launch_command from job " +
            "where job_type = 1 and dag_id = #{dagId}")
    JobDto selectStartJobByDagId(@Param("dagId") long dagId);

    @Select("<script>" +
            "select id,job_name,job_type,sharding_cnt,dag_id,schedule_type,launch_command from job where id in " +
            "<foreach item='item' index='index' collection='jobIdList' open='(' separator=', ' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    List<JobDto> selectJobDtoListByJobIds(@Param("jobIdList") List<Long> jobIdList);

}
