package com.simba.dongfeng.center.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.simba.dongfeng.center.dao.JobTriggerLogDao;
import com.simba.dongfeng.center.pojo.DagTriggerLogDto;
import com.simba.dongfeng.center.pojo.JobTriggerLogDto;
import com.simba.dongfeng.center.service.CenterJobLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DATE:   2019/9/2 10:47
 * AUTHOR: simba.hjf
 * DESC:
 **/
@Service
public class CenterJobLogServiceImpl implements CenterJobLogService {

    @Resource
    private JobTriggerLogDao jobTriggerLogDao;

    @Override
    public PageInfo<JobTriggerLogDto> selectJobLogByPage(int page, int pageSize, long dagLogId) {
        PageHelper.startPage(page, pageSize);
        List<JobTriggerLogDto> jobTriggerLogDtoList = Optional.ofNullable(jobTriggerLogDao.selectJobLogByPage(dagLogId)).orElse(new ArrayList<>());
        PageInfo<JobTriggerLogDto> pageInfo = new PageInfo<>(jobTriggerLogDtoList);
        return pageInfo;
    }
}
