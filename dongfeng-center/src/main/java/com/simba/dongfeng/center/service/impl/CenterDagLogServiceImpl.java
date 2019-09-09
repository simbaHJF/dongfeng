package com.simba.dongfeng.center.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.simba.dongfeng.center.dao.DagTriggerLogDao;
import com.simba.dongfeng.center.pojo.DagTriggerLogDto;
import com.simba.dongfeng.center.pojo.JobDto;
import com.simba.dongfeng.center.service.CenterDagLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author: simab.hjf
 * @description:
 * @date: Created in  2019-09-01 22:20
 */
@Service
public class CenterDagLogServiceImpl implements CenterDagLogService {

    @Resource
    private DagTriggerLogDao dagTriggerLogDao;

    @Override
    public PageInfo<DagTriggerLogDto> selectDagLogByPage(int page, int pageSize, long dagId) {
        PageHelper.startPage(page, pageSize);
        List<DagTriggerLogDto> dagTriggerLogDtoList = Optional.ofNullable(dagTriggerLogDao.selectDagLogByPage(dagId)).orElse(new ArrayList<>());
        PageInfo<DagTriggerLogDto> pageInfo = new PageInfo<>(dagTriggerLogDtoList);
        return pageInfo;
    }
}
