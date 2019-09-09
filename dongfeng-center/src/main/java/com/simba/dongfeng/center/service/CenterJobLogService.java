package com.simba.dongfeng.center.service;

import com.github.pagehelper.PageInfo;
import com.simba.dongfeng.center.pojo.JobTriggerLogDto;

/**
 * DATE:   2019/9/2 10:47
 * AUTHOR: simba.hjf
 * DESC:
 **/
public interface CenterJobLogService {
    PageInfo<JobTriggerLogDto> selectJobLogByPage(int page, int pageSize, long dagLogId);
}
