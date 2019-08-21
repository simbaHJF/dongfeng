package com.simba.dongfeng.center.dao;

import com.simba.dongfeng.common.pojo.ExecutorDto;
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

    @Insert("replace into executor(executor_name,executor_ip,executor_port,executor_group,active_time) " +
            "values(#{executorDto.executorName},#{executorDto.executorIp},#{executorDto.executorPort}," +
            "#{executorDto.executorGroup},#{executorDto.activeTime})")
    int replaceExecutor(@Param("executorDto") ExecutorDto executorDto);


    @Delete("delete from executor where active_time < #{deadlineTime}")
    int deleteExpiredExecutor(@Param("deadlineTime") Date deadlineTime);


    @Select("select id,executor_name,executor_ip,executor_port,executor_group,active_time from executor " +
            "where executor_group = #{group} ")
    List<ExecutorDto> selectExecutorsByGroup(@Param("group") String group);

}
