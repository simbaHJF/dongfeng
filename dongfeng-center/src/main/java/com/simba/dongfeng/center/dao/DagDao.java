package com.simba.dongfeng.center.dao;

import com.simba.dongfeng.center.pojo.DagDto;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * DATE:   2019-08-15 16:15
 * AUTHOR: simba.hjf
 * DESC:
 **/
@Mapper
public interface DagDao {

    @Select("select id,dag_name,dag_group,dag_cron,status,trigger_time,param,alarm from dongfeng_dag " +
            " where id = #{dagId}")
    DagDto selectDagById(@Param("dagId") long dagId);

    @Select("select id,dag_name,dag_group,dag_cron,status,trigger_time,param,alarm from dongfeng_dag " +
            " where status = 2 and trigger_time < #{endTimeline} order by trigger_time for update")
    List<DagDto> selectNeedTriggerDagWithLock(@Param("endTimeline") Date endTimeline);

    @Select("select id,dag_name,dag_group,dag_cron,status,trigger_time,param,alarm from dongfeng_dag")
    List<DagDto> selectAllDag();

    @Insert("insert into dongfeng_dag(dag_name,dag_group,dag_cron,status,param,alarm) " +
            "values(#{dag.dagName},#{dag.dagGroup},#{dag.dagCron},#{dag.status},#{dag.param},#{dag.alarm})")
    int insertDag(@Param("dag") DagDto dagDto);

    @Update("update dongfeng_dag set trigger_time = #{triggerTime} where id = #{id}")
    int updateDagTriggerTime(@Param("id") long id, @Param("triggerTime") Date triggerTime);


    @Update("update dongfeng_dag set dag_name = #{dag.dagName} , dag_group = #{dag.dagGroup} , dag_cron = #{dag.dagCron} " +
            ", status = #{dag.status} , param = #{dag.param}, alarm = #{dag.alarm} " +
            "where id = #{dag.id}")
    int updateDagInfo(@Param("dag") DagDto dagDto);

    @Delete("delete from dongfeng_dag where id = #{id}")
    int deleteDagInfo(@Param("id") long dagId);

}
