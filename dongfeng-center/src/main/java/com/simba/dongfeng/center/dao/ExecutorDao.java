package com.simba.dongfeng.center.dao;

import com.simba.dongfeng.center.pojo.ExecutorDto;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * DATE:   2019-08-14 17:09
 * AUTHOR: simba.hjf
 * DESC:
 **/
@Mapper
public interface ExecutorDao {

    @Insert("insert into dongfeng_executor(executor_name,executor_ip,executor_port,executor_group,active_time) " +
            "values(#{executorDto.executorName},#{executorDto.executorIp},#{executorDto.executorPort}," +
            "#{executorDto.executorGroup},#{executorDto.activeTime})")
    int insertExecutor(@Param("executorDto") ExecutorDto executorDto);

    @Update("update dongfeng_executor set executor_name = #{executor.executorName}, " +
            "executor_port = #{executor.executorPort}, executor_group = #{executor.executorGroup} " +
            ", active_time = #{executor.activeTime} " +
            "where ip = #{executor.executorIp}")
    int updateExecutor(@Param("executor") ExecutorDto executorDto);


    @Delete("delete from dongfeng_executor where active_time < #{deadlineTime}")
    int deleteExpiredExecutor(@Param("deadlineTime") Date deadlineTime);


    @Select("select id,executor_name,executor_ip,executor_port,executor_group,active_time from dongfeng_executor " +
            "where executor_group = #{group} ")
    List<ExecutorDto> selectExecutorsByGroup(@Param("group") String group);

    @Select("<script>" +
            "select id,executor_name,executor_ip,executor_port,executor_group,active_time from dongfeng_executor " +
            "where executor_ip = #{executorIp} " +
            "<when test='lock = true'> for update</when> " +
            "</script>")
    ExecutorDto selectExecutorByIp(@Param("executorIp") String executorIp, @Param("lock") boolean lock);


}
