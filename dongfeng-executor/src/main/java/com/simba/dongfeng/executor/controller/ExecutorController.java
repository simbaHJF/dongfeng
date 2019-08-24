package com.simba.dongfeng.executor.controller;

import com.simba.dongfeng.common.builder.RespDtoBuilder;
import com.simba.dongfeng.common.pojo.JobInfo;
import com.simba.dongfeng.common.pojo.RespDto;
import com.simba.dongfeng.executor.core.ExecutorCtrlCenter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.concurrent.RejectedExecutionException;

/**
 * DATE:   2019-08-22 10:25
 * AUTHOR: simba.hjf
 * DESC:
 **/
@Controller
public class ExecutorController {
    private Logger logger = LoggerFactory.getLogger(ExecutorController.class);

    @Resource
    private ExecutorCtrlCenter executorCtrlCenter;

    @RequestMapping("/job/trigger")
    public RespDto jobTrigger(JobInfo jobInfo) {
        logger.info("receive job trigger request.jobInfo:" + jobInfo);
        if (jobInfo == null || StringUtils.isBlank(jobInfo.getLaunchCommand())) {
            logger.error("ExecutorController#jobTrigger err,bad request.");
            return RespDtoBuilder.createBuilder().badReqResp().build();
        }
        try {
            executorCtrlCenter.jobTrigger(jobInfo);
        } catch (RejectedExecutionException e) {
            logger.error("server resource lacking,reject.jobInfo:" + jobInfo);
            return RespDtoBuilder.createBuilder().serverResourceLackResp().build();
        } catch (Exception e) {
            logger.error("internal server err.jobInfo:" + jobInfo);
            return RespDtoBuilder.createBuilder().serverErrResp().build();
        }
        return RespDtoBuilder.createBuilder().succResp().build();
    }
}
