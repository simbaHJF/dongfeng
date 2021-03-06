package com.simba.dongfeng.center.dao;

import com.simba.dongfeng.center.pojo.DagTriggerLogDto;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * DATE:   2019-08-19 10:56
 * AUTHOR: simba.hjf
 * DESC:
 **/
public interface DagTriggerLogDao {

    @Select("select id,dag_id,dag_name,trigger_type,start_time,end_time,status,param from dongfeng_dag_trigger_log " +
            " where id = #{dagTriggerId} ")
    DagTriggerLogDto selectDagTriggerLogById(@Param("dagTriggerId") long dagTriggerId);

    @Select("<script> " +
            "select id,dag_id,dag_name,trigger_type,start_time,end_time,status,param " +
            " from dongfeng_dag_trigger_log " +
            "<if test='dagId != 0'> where dag_id = #{dagId} </if> " +
            "</script> ")
    List<DagTriggerLogDto> selectDagLogByPage(@Param("dagId") long dagId);


    @Insert("insert into dongfeng_dag_trigger_log(dag_id,dag_name,trigger_type,start_time,status,param) " +
            " values(#{dagTrigerLog.dagId},#{dagTrigerLog.dagName},#{dagTrigerLog.triggerType},#{dagTrigerLog.startTime}," +
            "#{dagTrigerLog.status},#{dagTrigerLog.param})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int insertDagTriggerLog(@Param("dagTrigerLog") DagTriggerLogDto dagTrigerLog);


    @Update("<script>update dongfeng_dag_trigger_log set status = #{dagTrigerLog.status} " +
            "<when test='#{dagTrigerLog.endTime} != null'> , end_time = #{dagTrigerLog.endTime} </when>" +
            "where id =#{dagTrigerLog.id}" +
            "</script>")
    int updateDagTriggerLog(@Param("dagTrigerLog") DagTriggerLogDto dagTrigerLog);

    @Update("update dongfeng_dag_trigger_log set status = 1 where id = #{dagLogId}")
    int updateDagTriggerLogStatusInitial(@Param("dagLogId") long dagLogId);


    @Delete("delete from dongfeng_dag_trigger_log where end_time < #{timeLine} or start_time < #{timeLine}")
    int deleteExpiredDagLog(@Param("timeLine") Date timeLine);
}
