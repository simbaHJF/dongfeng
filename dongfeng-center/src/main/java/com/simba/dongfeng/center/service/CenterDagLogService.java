package com.simba.dongfeng.center.service;

import com.github.pagehelper.PageInfo;
import com.simba.dongfeng.center.pojo.DagTriggerLogDto;

import java.util.List;

/**
 * @author: simab.hjf
 * @description:
 * @date: Created in  2019-09-01 22:19
 */
public interface CenterDagLogService {
    PageInfo<DagTriggerLogDto> selectDagLogByPage(int page, int pageSize);
}
