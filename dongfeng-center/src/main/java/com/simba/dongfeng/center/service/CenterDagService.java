package com.simba.dongfeng.center.service;

import com.simba.dongfeng.center.pojo.DagDto;

import java.util.List;

/**
 * DATE:   2019/8/29 14:06
 * AUTHOR: simba.hjf
 * DESC:
 **/
public interface CenterDagService {
    int insertDag(DagDto dagDto);

    List<DagDto> selectAllDag();

    int updateDagInfo(DagDto dagDto);

    DagDto selectDagById(long dagId);

    int deleteDagInfo(long dagId);

}
