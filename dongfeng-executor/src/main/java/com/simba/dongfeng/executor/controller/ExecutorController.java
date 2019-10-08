package com.simba.dongfeng.executor.controller;

import com.simba.dongfeng.common.builder.RespDtoBuilder;
import com.simba.dongfeng.common.pojo.JobInfo;
import com.simba.dongfeng.common.pojo.RespDto;
import com.simba.dongfeng.executor.core.ExecutorCtrlCenter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @ResponseBody
    public RespDto jobTrigger(@RequestBody JobInfo jobInfo) {
        logger.info("receive job trigger request.jobInfo:" + jobInfo);
        if (jobInfo == null || StringUtils.isBlank(jobInfo.getLaunchCommand())) {
            logger.error("ExecutorController#jobTrigger err,bad request.");
            return RespDtoBuilder.createBuilder().badReqResp().build();
        }
        try {
            if (executorCtrlCenter.writeJobLogIdToRedisIfAbsent(jobInfo.getJobTriggerLogId())) {
                executorCtrlCenter.jobTrigger(jobInfo);
            } else {
                return RespDtoBuilder.createBuilder().repeatedRequestResp().build();
            }

        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            logger.error("server resource lacking,reject.jobInfo:" + jobInfo, e);
            executorCtrlCenter.deleteJobRecordInPool(jobInfo.getJobTriggerLogId());
            executorCtrlCenter.deleteJobLogIdKeyInRedis(jobInfo.getJobTriggerLogId());
            return RespDtoBuilder.createBuilder().serverResourceLackResp().build();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("internal server err.jobInfo:" + jobInfo, e);
            executorCtrlCenter.deleteJobRecordInPool(jobInfo.getJobTriggerLogId());
            executorCtrlCenter.deleteJobLogIdKeyInRedis(jobInfo.getJobTriggerLogId());
            return RespDtoBuilder.createBuilder().serverErrResp().build();
        }
        return RespDtoBuilder.createBuilder().succResp().build();
    }

    @RequestMapping("/job/check")
    @ResponseBody
    public RespDto jobCheck(@RequestBody JobInfo jobInfo) {
        logger.info("recv job check request,jobInfo:" + jobInfo);
        if (executorCtrlCenter.checkJob(jobInfo.getJobTriggerLogId())) {
            return RespDtoBuilder.createBuilder().checkSuccResp().build();
        } else {
            return RespDtoBuilder.createBuilder().checkFailResp().build();
        }
    }

    @RequestMapping("/job/interrupt")
    @ResponseBody
    public RespDto jobInterrupt(@RequestBody long jobLogId) {
        logger.info("recv job interrupt request.jobLogId:" + jobLogId);
        executorCtrlCenter.jobInterrupt(jobLogId);
        return RespDtoBuilder.createBuilder().succResp().build();
    }
}
