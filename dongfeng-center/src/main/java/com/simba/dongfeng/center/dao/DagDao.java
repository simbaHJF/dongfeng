package com.simba.dongfeng.center.dao;

import com.simba.dongfeng.common.pojo.DagDto;
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

    @Select("select id,dag_name,dag_group,dag_cron,status,trigger_time,param from dag " +
            " where id = #{dagId}")
    DagDto selectDagById(@Param("dagId") long dagId);

    @Select("select id,dag_name,dag_group,dag_cron,status,trigger_time,param from dag " +
            " where status = 1 and trigger_time < #{timeline} order by trigger_time for update")
    List<DagDto> selectNeedTriggerDagWithLock(@Param("timeline") Date timeline);

    @Update("update dag set trigger_time = #{triggerTime} where id = #{id}")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int updateDagTriggerTime(@Param("id") long id, @Param("triggerTime") Date triggerTime);

}
