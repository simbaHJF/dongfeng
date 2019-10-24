package com.simba.dongfeng.center.service.impl;

import com.simba.dongfeng.center.converter.PojoConverter;
import com.simba.dongfeng.center.dao.ExecutorDao;
import com.simba.dongfeng.center.service.CenterExecutorService;
import com.simba.dongfeng.center.pojo.ExecutorDto;
import com.simba.dongfeng.common.pojo.ExecutorHeartbeatInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private Logger logger = LoggerFactory.getLogger(CenterExecutorServiceImpl.class);
    @Resource
    private ExecutorDao executorDao;


    @Override
    @Transactional(value = "transactionManager")
    public int heartbeatHandle(ExecutorHeartbeatInfo executorHeartbeatInfo) {
        ExecutorDto heartbeatExecutorDto = PojoConverter.convertExecutorHeartbeatInfo(executorHeartbeatInfo);
        ExecutorDto executorDto = executorDao.selectExecutorByIp(heartbeatExecutorDto.getExecutorIp(), true);
        if (executorDto != null) {
            logger.info("in update transactionManager,heartbeatExecutorDto:" + heartbeatExecutorDto);
            return executorDao.updateExecutor(heartbeatExecutorDto);
        } else {
            logger.info("in insert transactionManager,heartbeatExecutorDto:" + heartbeatExecutorDto);
            return executorDao.insertExecutor(heartbeatExecutorDto);
        }
    }
}
