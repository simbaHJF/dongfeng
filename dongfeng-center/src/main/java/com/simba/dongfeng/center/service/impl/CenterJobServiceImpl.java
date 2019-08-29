package com.simba.dongfeng.center.service.impl;

import com.simba.dongfeng.center.dao.JobDao;
import com.simba.dongfeng.center.pojo.JobDto;
import com.simba.dongfeng.center.service.CenterJobService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * DATE:   2019/8/29 20:37
 * AUTHOR: simba.hjf
 * DESC:
 **/
@Service
public class CenterJobServiceImpl implements CenterJobService {

    @Resource
    private JobDao jobDao;

    @Override
    public List<JobDto> selectAllJob() {
        return jobDao.selectAllJob();
    }
}
