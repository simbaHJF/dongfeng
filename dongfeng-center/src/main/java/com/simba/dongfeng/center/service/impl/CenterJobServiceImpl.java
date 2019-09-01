package com.simba.dongfeng.center.service.impl;

import com.simba.dongfeng.center.dao.DependencyDao;
import com.simba.dongfeng.center.dao.JobDao;
import com.simba.dongfeng.center.enums.JobTypeEnum;
import com.simba.dongfeng.center.pojo.DependencyDto;
import com.simba.dongfeng.center.pojo.JobDto;
import com.simba.dongfeng.center.service.CenterJobService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Resource
    private DependencyDao dependencyDao;

    @Override
    public List<JobDto> selectAllJob() {
        return jobDao.selectAllJob();
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

}
