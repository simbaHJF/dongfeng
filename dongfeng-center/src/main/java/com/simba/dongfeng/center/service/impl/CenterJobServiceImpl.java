package com.simba.dongfeng.center.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.simba.dongfeng.center.dao.DependencyDao;
import com.simba.dongfeng.center.dao.JobDao;
import com.simba.dongfeng.center.enums.JobTypeEnum;
import com.simba.dongfeng.center.pojo.DependencyDto;
import com.simba.dongfeng.center.pojo.JobDto;
import com.simba.dongfeng.center.service.CenterJobService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * DATE:   2019/8/29 20:37
 * AUTHOR: simba.hjf
 * DESC:
 **/
@Service
public class CenterJobServiceImpl implements CenterJobService {

    @Resource
    private JobDao jobDao;
    @Resource
    private DependencyDao dependencyDao;

    @Override
    public PageInfo<JobDto> selectJobByPage(int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<JobDto> jobDtoList = Optional.ofNullable(jobDao.selectAllJob()).orElse(new ArrayList<>());
        PageInfo<JobDto> pageInfo = new PageInfo<>(jobDtoList);
        return pageInfo;
    }

    @Override
    @Transactional(value = "transactionManager")
    public void insertJobAndDependency(JobDto jobDto) {

        jobDao.insertJob(jobDto);
        if (jobDto.getJobType() == JobTypeEnum.START_NODE.getValue()) {
            return;
        }
        String parentJobIds = jobDto.getParentJobIds();
            String[] parentJobIdArr = parentJobIds.split(",");
            for (String parentJobId : parentJobIdArr) {
                DependencyDto dependencyDto = new DependencyDto();
                dependencyDto.setJobId(jobDto.getId());
                dependencyDto.setParentJobId(Long.parseLong(parentJobId));
                dependencyDto.setDagId(jobDto.getDagId());
                dependencyDao.insertDependency(dependencyDto);
            }
    }

    @Override
    public JobDto selectJobById(long jobId) {
        return jobDao.selectJobById(jobId);
    }

    @Override
    public int updateJob(JobDto jobDto) {
        return jobDao.updateJob(jobDto);
    }

    @Override
    public int deleteJob(long jobId) {
        List<Long> childDependencyList = dependencyDao.selectChildJobIdList(jobId);
        if (CollectionUtils.isNotEmpty(childDependencyList)) {
            return 0;
        }
        return jobDao.deleteJob(jobId);
    }

}
