package com.simba.dongfeng.center.dao;

import com.simba.dongfeng.center.pojo.JobDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * DATE:   2019-08-15 16:52
 * AUTHOR: simba.hjf
 * DESC:
 **/
@Mapper
public interface JobDao {

    @Select("select id,job_name,job_type,sharding_cnt,dag_id,schedule_type,launch_command,assign_ip from dongfeng_job " +
            "where id = #{jobId}")
    JobDto selectJobById(@Param("jobId") long jobId);

    @Select("select id,job_name,job_type,sharding_cnt,dag_id,schedule_type,launch_command from dongfeng_job " +
            "where job_type = 1 and dag_id = #{dagId}")
    JobDto selectStartJobByDagId(@Param("dagId") long dagId);

    @Select("<script> " +
            "select id,job_name,job_type,sharding_cnt,dag_id,schedule_type,launch_command,assign_ip from dongfeng_job " +
            "<when test = 'dagId != 0'> where dag_id = #{dagId} </when> " +
            "</script>")
    List<JobDto> selectJobsByPage(@Param("dagId") long dagId);

    @Select("<script>" +
            "select id,job_name,job_type,sharding_cnt,dag_id,schedule_type,launch_command,assign_ip from dongfeng_job where id in " +
            "<foreach item='item' index='index' collection='jobIdList' open='(' separator=', ' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    List<JobDto> selectJobDtoListByJobIds(@Param("jobIdList") List<Long> jobIdList);

    @Insert("insert into dongfeng_job(job_name,job_type,sharding_cnt,dag_id,schedule_type,launch_command,assign_ip) " +
            "values(#{job.jobName},#{job.jobType},#{job.shardingCnt},#{job.dagId},#{job.scheduleType}," +
            "#{job.launchCommand},#{job.assignIp})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int insertJob(@Param("job") JobDto jobDto);

    @Update("update dongfeng_job set job_name = #{job.jobName},job_type = #{job.jobType},sharding_cnt = #{job.shardingCnt},dag_id = #{job.dagId}," +
            "schedule_type = #{job.scheduleType},launch_command = #{job.launchCommand},assign_ip = #{job.assignIp} " +
            "where id = #{job.id}")
    int updateJob(@Param("job") JobDto jobDto);

    @Delete("delete from dongfeng_job where id =#{jobId}")
    int deleteJob(@Param("jobId") long jobId);

    @Delete("delete from dongfeng_job where dag_id = #{dagId}")
    int deleteJobByDagId(@Param("dagId") long dagId);
}
