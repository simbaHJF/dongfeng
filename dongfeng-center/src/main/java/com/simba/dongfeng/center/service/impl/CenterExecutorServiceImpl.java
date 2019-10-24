package com.simba.dongfeng.center.service.impl;

import com.simba.dongfeng.center.converter.PojoConverter;
import com.simba.dongfeng.center.dao.ExecutorDao;
import com.simba.dongfeng.center.service.CenterExecutorService;
import com.simba.dongfeng.center.pojo.ExecutorDto;
import com.simba.dongfeng.common.pojo.ExecutorHeartbeatInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(value = "transactionManager")
    public int heartbeatHandle(ExecutorHeartbeatInfo executorHeartbeatInfo) {
        ExecutorDto heartbeatExecutorDto = PojoConverter.convertExecutorHeartbeatInfo(executorHeartbeatInfo);
        ExecutorDto executorDto = executorDao.selectExecutorByIp(heartbeatExecutorDto.getExecutorIp(), true);
        if (executorDto != null) {
            return executorDao.updateExecutor(heartbeatExecutorDto);
        } else {
            return executorDao.insertExecutor(heartbeatExecutorDto);
        }
    }
}
