package com.simba.dongfeng.center.dao;

import com.simba.dongfeng.center.pojo.JobTriggerLogDto;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * DATE:   2019-08-19 13:49
 * AUTHOR: simba.hjf
 * DESC:
 **/
public interface JobTriggerLogDao {


    @Select("select id,job_id,job_name,dag_id,dag_trigger_id,start_time,end_time,status,center_ip,executor_ip," +
            "sharding_idx,sharding_cnt,param " +
            "from dongfeng_job_trigger_log where id = #{id}")
    JobTriggerLogDto selectJobTriggerLogDtoById(@Param("id") long id);

    @Select("<script> " +
            "select id,job_id,job_name,dag_id,dag_trigger_id,start_time,end_time,status,center_ip,executor_ip," +
            "sharding_idx,sharding_cnt,param " +
            " from dongfeng_job_trigger_log " +
            "<when test = 'dagLogId != 0'> where dag_trigger_id = #{dagLogId} </when>" +
            "</script>")
    List<JobTriggerLogDto> selectJobLogs(@Param("dagLogId") long dagLogId);

    @Select("<script> " +
            "select id,job_id,job_name,dag_id,dag_trigger_id,start_time,end_time,status,center_ip,executor_ip," +
            "sharding_idx,sharding_cnt,param  " +
            "from dongfeng_job_trigger_log where job_id = #{jobId} and dag_trigger_id = #{dagTriggerId} " +
            "<when test='lock = true'> for update</when> " +
            "</script>")
    JobTriggerLogDto selectJobTriggerLogDtoByJobAndDag(@Param("jobId") long jobId,
                                                       @Param("dagTriggerId") long dagTriggerId,
                                                       @Param("lock") boolean lock);

    @Insert("insert into dongfeng_job_trigger_log(job_id,job_name,dag_id,dag_trigger_id,start_time,end_time,status," +
            "center_ip,executor_ip,sharding_idx,sharding_cnt,param) " +
            "values(#{jobTriggerLog.jobId},#{jobTriggerLog.jobName},#{jobTriggerLog.dagId},#{jobTriggerLog" +
            ".dagTriggerId},#{jobTriggerLog.startTime},#{jobTriggerLog.endTime}, " +
            "#{jobTriggerLog.status},#{jobTriggerLog.centerIp},#{jobTriggerLog.executorIp},#{jobTriggerLog" +
            ".shardingIdx}," +
            "#{jobTriggerLog.shardingCnt},#{jobTriggerLog.param})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int inserJobTriggerLog(@Param("jobTriggerLog") JobTriggerLogDto jobTriggerLogDto);

    @Update("<script> " +
            "update dongfeng_job_trigger_log set status = #{jobTriggerLog.status}  " +
            "<when test='#{jobTriggerLog.executorIp} != null'> , executor_ip = #{jobTriggerLog.executorIp} </when>" +
            "<when test='#{jobTriggerLog.endTime} != null'> , end_time = #{jobTriggerLog.endTime} </when>" +
            "where id = #{jobTriggerLog.id} " +
            "</script>")
    int updateJobTriggerLog(@Param("jobTriggerLog") JobTriggerLogDto jobTriggerLogDto);

    @Update("<script> " +
            "update dongfeng_job_trigger_log set status = #{jobTriggerLog.status}  " +
            "<when test='#{jobTriggerLog.executorIp} != null'> , executor_ip = #{jobTriggerLog.executorIp} </when>" +
            "<when test='#{jobTriggerLog.endTime} != null'> , end_time = #{jobTriggerLog.endTime} </when>" +
            "where id = #{jobTriggerLog.id} and center_ip = #{jobTriggerLog.centerIp}" +
            "</script>")
    int updateJobTriggerLogWithAssignedCenter(@Param("jobTriggerLog") JobTriggerLogDto jobTriggerLogDto);


    @Update("<script>update dongfeng_job_trigger_log set status = #{jobTriggerLog.status}  " +
            "<when test='#{jobTriggerLog.endTime} != null'> , end_time = #{jobTriggerLog.endTime} </when>" +
            "<when test='#{jobTriggerLog.executorIp} != null'> , executor_ip = #{jobTriggerLog.executorIp} </when>" +
            "where id = #{jobTriggerLog.id} and status in " +
            "<foreach item='item' index='index' collection='expectStatus' open='(' separator=', ' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    int updateJobTriggerLogWithAssignedStatus(@Param("jobTriggerLog") JobTriggerLogDto jobTriggerLogDto, @Param(
            "expectStatus") List<Integer> expectStatus);


    @Delete("delete from dongfeng_job_trigger_log where end_time < #{timeLine} or start_time < #{timeLine}")
    int deleteExpiredJobLog(@Param("timeLine") Date timeLine);

    @Delete("delete from dongfeng_job_trigger_log where id = #{jobLogId}")
    int deleteJobLog(@Param("jobLogId") long jobLogId);
}
