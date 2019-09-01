package com.simba.dongfeng.center.service;

import com.github.pagehelper.PageInfo;
import com.simba.dongfeng.center.pojo.DagDto;

import java.util.List;

/**
 * DATE:   2019/8/29 14:06
 * AUTHOR: simba.hjf
 * DESC:
 **/
public interface CenterDagService {
    int insertDag(DagDto dagDto);

    PageInfo<DagDto> selectDagByPage(int page, int pageSize);

    int updateDagInfo(DagDto dagDto);

    DagDto selectDagById(long dagId);

    void deleteDagInfo(long dagId);

}
