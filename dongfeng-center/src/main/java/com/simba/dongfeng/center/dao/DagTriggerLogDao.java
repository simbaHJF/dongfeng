package com.simba.dongfeng.center.dao;

import com.simba.dongfeng.center.pojo.DagTriggerLogDto;
import org.apache.ibatis.annotations.*;

/**
 * DATE:   2019-08-19 10:56
 * AUTHOR: simba.hjf
 * DESC:
 **/
public interface DagTriggerLogDao {

    @Select("select id,dag_id,trigger_type,start_time,end_time,status,param from dag_trigger_log " +
            " where id = #{dagTriggerId} ")
    DagTriggerLogDto selectDagTriggerLogById(@Param("dagTriggerId") long dagTriggerId);


    @Insert("insert into dag_trigger_log(dag_id,trigger_type,start_time,status,param) " +
            " values(#{dagTrigerLog.dagId},#{dagTrigerLog.triggerType},#{dagTrigerLog.startTime}," +
            "#{dagTrigerLog.status},#{dagTrigerLog.param})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int insertDagTriggerLog(@Param("dagTrigerLog") DagTriggerLogDto dagTrigerLog);


    @Update("<script>update dag_trigger_log set status = #{dagTrigerLog.status} " +
            "<when test='#{dagTrigerLog.endTime} != null'> and end_time = #{dagTrigerLog.endTime} </when>" +
            "where id =#{dagTrigerLog.id}" +
            "</script>")
    int updateDagTriggerLog(@Param("dagTrigerLog") DagTriggerLogDto dagTrigerLog);
}
