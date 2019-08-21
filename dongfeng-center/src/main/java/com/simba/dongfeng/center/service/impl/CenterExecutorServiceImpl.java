package com.simba.dongfeng.center.service.impl;

import com.simba.dongfeng.center.dao.ExecutorDao;
import com.simba.dongfeng.center.service.CenterExecutorService;
import com.simba.dongfeng.common.pojo.ExecutorDto;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * DATE:   2019-08-14 17:29
 * AUTHOR: simba.hjf
 * DESC:
 **/
@Service
public class CenterExecutorServiceImpl implements CenterExecutorService {

    @Resource
    private ExecutorDao executorDao;

    @Override
    public int replaceExecutor(ExecutorDto executorDto) {
        return executorDao.replaceExecutor(executorDto);
    }
}
