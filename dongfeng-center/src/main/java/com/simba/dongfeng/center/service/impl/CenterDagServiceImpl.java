package com.simba.dongfeng.center.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.simba.dongfeng.center.core.CronExpression;
import com.simba.dongfeng.center.dao.DagDao;
import com.simba.dongfeng.center.dao.DependencyDao;
import com.simba.dongfeng.center.dao.JobDao;
import com.simba.dongfeng.center.enums.DagSwitchStatusEnum;
import com.simba.dongfeng.center.pojo.DagDto;
import com.simba.dongfeng.center.service.CenterDagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * DATE:   2019/8/29 14:06
 * AUTHOR: simba.hjf
 * DESC:
 **/
@Service
public class CenterDagServiceImpl implements CenterDagService {
    @Resource
    private DagDao dagDao;
    @Resource
    private DependencyDao dependencyDao;
    @Resource
    private JobDao jobDao;

    public int insertDag(DagDto dagDto) {
        return dagDao.insertDag(dagDto);
    }

    public PageInfo<DagDto> selectDagByPage(int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<DagDto> dagDtoList = Optional.ofNullable(dagDao.selectAllDag()).orElse(new ArrayList<>());
        PageInfo<DagDto> pageInfo = new PageInfo<>(dagDtoList);
        return pageInfo;
    }

    @Override
    public int updateDagInfo(DagDto dagDto) {
        DagDto oldDag = dagDao.selectDagById(dagDto.getId());
        if (dagDto.getStatus() == DagSwitchStatusEnum.OFF.getValue()) {
            dagDao.updateDagTriggerTime(dagDto.getId(), null);
        } else {
            try {
                /*ZoneId zoneId = ZoneId.of("GMT+08");*/
                LocalDateTime localDateTime = LocalDateTime.now();
                Date triggerTime = new CronExpression(dagDto.getDagCron()).getNextValidTimeAfter(Timestamp.valueOf(localDateTime));
                dagDao.updateDagTriggerTime(dagDto.getId(), triggerTime);
            } catch (Exception e) {
                throw new RuntimeException("updateDagInfo err", e);
            }
        }
        return dagDao.updateDagInfo(dagDto);
    }

    @Override
    public DagDto selectDagById(long dagId) {
        return dagDao.selectDagById(dagId);
    }

    @Override
    @Transactional("transactionManager")
    public void deleteDagInfo(long dagId) {
        dependencyDao.deleteDependencyByDagId(dagId);
        jobDao.deleteJobByDagId(dagId);
        dagDao.deleteDagInfo(dagId);
    }
}
