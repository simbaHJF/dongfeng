package com.simba.dongfeng.center.service.impl;

import com.simba.dongfeng.center.dao.DependencyDao;
import com.simba.dongfeng.center.service.CenterDependencyService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * DATE:   2019/8/29 20:56
 * AUTHOR: simba.hjf
 * DESC:
 **/
@Service
public class CenterDependencyServiceImpl implements CenterDependencyService {

    @Resource
    private DependencyDao dependencyDao;

    @Override
    public List<Long> selectParentJobIdList(long jobId) {
        return dependencyDao.selectParentJobIdList(jobId);
    }
}
