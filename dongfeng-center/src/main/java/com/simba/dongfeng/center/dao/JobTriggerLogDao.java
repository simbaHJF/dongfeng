package com.simba.dongfeng.center.dao;

import com.simba.dongfeng.center.pojo.JobTriggerLogDto;
import org.apache.ibatis.annotations.*;

/**
 * DATE:   2019-08-19 13:49
 * AUTHOR: simba.hjf
 * DESC:
 **/
public interface JobTriggerLogDao {


    @Select("select id,job_id,dag_id,dag_trigger_id,start_time,end_time,status,center_ip,executor_ip,sharding_idx,sharding_cnt,param " +
            "from job_trigger_log where id = #{id}")
    JobTriggerLogDto selectJobTriggerLogDtoById(@Param("id") long id);

    @Select("select id,job_id,dag_id,dag_trigger_id,start_time,end_time,status,center_ip,executor_ip,sharding_idx,sharding_cnt,param  " +
            "from job_trigger_log where job_id = #{jobId} and dag_trigger_id = #{dagTriggerId} " +
            "<when test='lock = true'> for update</when>")
    JobTriggerLogDto selectJobTriggerLogDtoByJobAndDag(@Param("jobId") long jobId,@Param("dagTriggerId") long dagTriggerId,@Param("lock") boolean lock);

    @Insert("insert into job_trigger_log(job_id,dag_id,dag_trigger_id,start_time,status,center_ip,executor_ip,sharding_idx,sharding_cnt,param) " +
            "values(#{jobTriggerLog.jobId},#{jobTriggerLog.dagId},#{jobTriggerLog.dagTriggerId},#{jobTriggerLog.startTime}," +
            "#{jobTriggerLog.status},#{jobTriggerLog.centerIp},#{jobTriggerLog.executorIp},#{jobTriggerLog.shardingIdx}," +
            "#{jobTriggerLog.shardingCnt},#{jobTriggerLog.param})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int inserJobTriggerLog(@Param("jobTriggerLog") JobTriggerLogDto jobTriggerLogDto);

    @Update("<script>update job_trigger_log set status = #{jobTriggerLogDto.status}  " +
            "<when test='#{jobTriggerLogDto.executorIp} != null'> , executor_ip = #{jobTriggerLogDto.executorIp} </when>" +
            "<when test='#{jobTriggerLogDto.endTime} != null'> , end_time = #{jobTriggerLogDto.endTime} </when>" +
            "where id = #{jobTriggerLogDto.id}" +
            "</script>")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int updateJobTriggerLog(@Param("jobTriggerLog") JobTriggerLogDto jobTriggerLogDto);

    @Update("<script>update job_trigger_log set status = #{jobTriggerLogDto.status}  " +
            "<when test='#{jobTriggerLogDto.executorIp} != null'> , executor_ip = #{jobTriggerLogDto.executorIp} </when>" +
            "<when test='#{jobTriggerLogDto.endTime} != null'> , end_time = #{jobTriggerLogDto.endTime} </when>" +
            "where id = #{jobTriggerLogDto.id} and center_ip = #{jobTriggerLog.centerIp}" +
            "</script>")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int updateJobTriggerLogWithAssignedCenter(@Param("jobTriggerLog") JobTriggerLogDto jobTriggerLogDto);


    @Update("<script>update job_trigger_log set status = #{jobTriggerLogDto.status}  " +
            "<when test='#{jobTriggerLogDto.endTime} != null'> , end_time = #{jobTriggerLogDto.endTime} </when>" +
            "where id = #{jobTriggerLogDto.id} and status = #{expectStatus}" +
            "</script>")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int updateJobTriggerLogWithAssignedStatus(@Param("jobTriggerLog") JobTriggerLogDto jobTriggerLogDto,@Param("expectStatus") int expectStatus);


}
