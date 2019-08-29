package com.simba.dongfeng.center.service.impl;

import com.simba.dongfeng.center.dao.DagDao;
import com.simba.dongfeng.center.pojo.DagDto;
import com.simba.dongfeng.center.service.CenterDagService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * DATE:   2019/8/29 14:06
 * AUTHOR: simba.hjf
 * DESC:
 **/
@Service
public class CenterDagServiceImpl implements CenterDagService {
    @Resource
    private DagDao dagDao;

    public int insertDag(DagDto dagDto) {
        return dagDao.insertDag(dagDto);
    }

    public List<DagDto> selectAllDag() {
        return dagDao.selectAllDag();
    }

    @Override
    public int updateDagInfo(DagDto dagDto) {

        return dagDao.updateDagInfo(dagDto);
    }

    @Override
    public DagDto selectDagById(long dagId) {
        return dagDao.selectDagById(dagId);
    }

    @Override
    public int deleteDagInfo(long dagId) {
        return dagDao.deleteDagInfo(dagId);
    }
}
