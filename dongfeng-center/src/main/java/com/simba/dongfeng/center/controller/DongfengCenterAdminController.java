package com.simba.dongfeng.center.controller;

import com.github.pagehelper.PageInfo;
import com.simba.dongfeng.center.core.ScheduleCenter;
import com.simba.dongfeng.center.enums.DagSwitchStatusEnum;
import com.simba.dongfeng.center.enums.ExecutorRouterStgEnum;
import com.simba.dongfeng.center.enums.JobTypeEnum;
import com.simba.dongfeng.center.pojo.DagDto;
import com.simba.dongfeng.center.pojo.DagTriggerLogDto;
import com.simba.dongfeng.center.pojo.JobDto;
import com.simba.dongfeng.center.pojo.JobTriggerLogDto;
import com.simba.dongfeng.center.service.*;
import com.simba.dongfeng.common.builder.RespDtoBuilder;
import com.simba.dongfeng.common.enums.RespCodeEnum;
import com.simba.dongfeng.common.pojo.RespDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * DATE:   2019/8/30 17:54
 * AUTHOR: simba.hjf
 * DESC:  管理后台crud相关操作controller
 **/
@Controller
@RequestMapping("/admin")
public class DongfengCenterAdminController {
    private Logger logger = LoggerFactory.getLogger(DongfengCenterAdminController.class);

    public static final int pageSize = 20;

    @Resource
    private ScheduleCenter scheduleCenter;

    @Resource
    private CenterDagService centerDagService;
    @Resource
    private CenterJobService centerJobService;
    @Resource
    private CenterDependencyService centerDependencyService;
    @Resource
    private CenterDagLogService centerDagLogService;
    @Resource
    private CenterJobLogService centerJobLogService;


    @RequestMapping("/manualTrigger")
    @ResponseBody
    public RespDto manualTrigger(long dagId, String manualTriggerDagParam) {
        logger.info("recv manualTrigger.dagId:" + dagId + ",manualTriggerDagParam:" + manualTriggerDagParam);
        if (dagId == 0) {
            return RespDtoBuilder.createBuilder().badReqResp().build();
        }
        scheduleCenter.manualTrigger(dagId, manualTriggerDagParam);
        return RespDtoBuilder.createBuilder().succResp().build();
    }

    @RequestMapping("/manualInterrupt")
    @ResponseBody
    public RespDto manualInterrupt(long dagLogId) {
        logger.info("recv manualInterrupt.dagLogId:" + dagLogId);
        if (dagLogId == 0) {
            return RespDtoBuilder.createBuilder().badReqResp().build();
        }
        scheduleCenter.manualInterrupt(dagLogId);
        return RespDtoBuilder.createBuilder().succResp().build();
    }

    @RequestMapping("/manualRerunFailDagLog")
    @ResponseBody
    public RespDto manualRerunFailDagLog(long dagLogId) {
        logger.info("recv manualRerunFailDagLog.dagLogId:" + dagLogId);
        if (dagLogId == 0) {
            return RespDtoBuilder.createBuilder().badReqResp().build();
        }
        try {
            scheduleCenter.manualRerunFailDagLog(dagLogId);
            return RespDtoBuilder.createBuilder().succResp().build();
        } catch (Exception e) {
            logger.error("manualRerunFailDagLog err.dagLogId:" + dagLogId, e);
            return RespDtoBuilder.createBuilder().serverErrResp().build();
        }
    }


    @RequestMapping("/index")
    public String index() {
        return "index";
    }


    @RequestMapping("/dagIndex")
    public String dagIndex() {
        return "dag";
    }

    @RequestMapping("/dagData")
    @ResponseBody
    public RespDto dagData(int page) {
        PageInfo<DagDto> pageInfo = centerDagService.selectDagByPage(page, pageSize);
        RespDto<PageInfo<DagDto>> respDto = new RespDto<>(RespCodeEnum.SUCC.getCode(), RespCodeEnum.SUCC.getMsg(),
                pageInfo);
        return respDto;
    }

    @RequestMapping("/addDag")
    @ResponseBody
    public RespDto addDag(DagDto dagDto) {
        if (dagDto.getStatus() == DagSwitchStatusEnum.ON.getValue()) {
            dagDto.setStatus(DagSwitchStatusEnum.OFF.getValue());
        }
        centerDagService.insertDag(dagDto);
        RespDto respDto = new RespDto<>(RespCodeEnum.SUCC.getCode(), RespCodeEnum.SUCC.getMsg());
        return respDto;
    }

    @RequestMapping("/updateDagPage")
    public String updateDagPage(Model model, long dagId) {
        DagDto dagDto = centerDagService.selectDagById(dagId);
        model.addAttribute("dagInfo", dagDto);
        return "updateDag";
    }


    @RequestMapping("/updateDagInfo")
    @ResponseBody
    public RespDto updateDagInfo(DagDto dagDto) {
        centerDagService.updateDagInfo(dagDto);
        return RespDtoBuilder.createBuilder().succResp().build();
    }

    @RequestMapping("/deleteDagInfo")
    @ResponseBody
    public RespDto deleteDagInfo(@RequestParam("dagId") long dagId) {
        centerDagService.deleteDagInfo(dagId);
        return RespDtoBuilder.createBuilder().succResp().build();
    }

    @RequestMapping("/jobIndex")
    public String jobIndex() {
        return "job";
    }

    @RequestMapping("/jobData")
    @ResponseBody
    public RespDto jobData(int page, long dagId) {
        PageInfo<JobDto> pageInfo = centerJobService.selectJobByPage(page, pageSize, dagId);
        for (JobDto jobDto : pageInfo.getList()) {
            List<Long> parentJobIds =
                    Optional.ofNullable(centerDependencyService.selectParentJobIdList(jobDto.getId())).orElse(new ArrayList<>());

            jobDto.setParentJobIds(parentJobIds.stream().map(ele -> String.valueOf(ele)).collect(Collectors.joining(
                    ",")));
        }
        RespDto<PageInfo> respDto = new RespDto<>(RespCodeEnum.SUCC.getCode(), RespCodeEnum.SUCC.getMsg(), pageInfo);
        return respDto;
    }

    @RequestMapping("/addJob")
    @ResponseBody
    public RespDto addJob(JobDto jobDto) {
        if (jobDto.getJobType() == JobTypeEnum.TASK_NODE.getValue() && StringUtils.isBlank(jobDto.getLaunchCommand())) {
            RespDto respDto = RespDtoBuilder.createBuilder().badReqResp().build();
            respDto.setMsg("任务节点类型为:任务节点 时,Launch command不能为空");
            return respDto;
        }
        if (jobDto.getJobType() != JobTypeEnum.START_NODE.getValue() && StringUtils.isBlank(jobDto.getParentJobIds())) {
            RespDto respDto = RespDtoBuilder.createBuilder().badReqResp().build();
            respDto.setMsg("任务节点类型非开始节点时,Parent job ids不能为空");
            return respDto;
        }
        if (jobDto.getScheduleType() == ExecutorRouterStgEnum.ASSIGN.getRouterName() && StringUtils.isBlank(jobDto.getAssignIp())) {
            RespDto respDto = RespDtoBuilder.createBuilder().badReqResp().build();
            respDto.setMsg("调度方式为:指定ip 时,Assign ip不能为空");
            return respDto;
        }
        DagDto dagDto = centerDagService.selectDagById(jobDto.getDagId());
        if (dagDto == null) {
            RespDto respDto = RespDtoBuilder.createBuilder().badReqResp().build();
            respDto.setMsg("对应dag不存在");
            return respDto;
        }
        if (!StringUtils.equals(ExecutorRouterStgEnum.ASSIGN.getRouterName(), jobDto.getScheduleType())) {
            jobDto.setAssignIp("");
        }
        centerJobService.insertJobAndDependency(jobDto);
        RespDto respDto = new RespDto<>(RespCodeEnum.SUCC.getCode(), RespCodeEnum.SUCC.getMsg());
        return respDto;
    }

    @RequestMapping("/updateJobPage")
    public String updateJobPage(Model model, long jobId) {
        JobDto jobDto = centerJobService.selectJobById(jobId);
        List<Long> parentJobIds =
                Optional.ofNullable(centerDependencyService.selectParentJobIdList(jobId)).orElse(new ArrayList<>());
        jobDto.setParentJobIds(parentJobIds.stream().map(ele -> String.valueOf(ele)).collect(Collectors.joining(",")));
        model.addAttribute("jobInfo", jobDto);
        return "updateJob";
    }

    @RequestMapping("/updateJobInfo")
    @ResponseBody
    public RespDto updateJobInfo(JobDto jobDto) {
        if (StringUtils.isBlank(jobDto.getAssignIp())) {
            jobDto.setAssignIp("");
        }

        if (!StringUtils.equals(ExecutorRouterStgEnum.ASSIGN.getRouterName(), jobDto.getScheduleType())) {
            jobDto.setAssignIp("");
        }
        centerJobService.updateJob(jobDto);
        return RespDtoBuilder.createBuilder().succResp().build();
    }


    @RequestMapping("/deleteJobInfo")
    @ResponseBody
    public RespDto deleteJobInfo(@RequestParam("jobId") long jobId) {
        boolean rs = centerJobService.deleteJob(jobId);

        if (rs == false) {
            RespDto respDto = RespDtoBuilder.createBuilder().badReqResp().build();
            respDto.setMsg("删除有误,请检查被删除job是否有子job.");
            return respDto;
        }
        return RespDtoBuilder.createBuilder().succResp().build();
    }


    @RequestMapping("/dagLogIndex")
    public String dagLogIndex() {
        return "dagLog";
    }

    @RequestMapping("/dagLogData")
    @ResponseBody
    public RespDto dagLogData(int page, long dagId) {
        PageInfo<DagTriggerLogDto> pageInfo = centerDagLogService.selectDagLogByPage(page, pageSize, dagId);
        RespDto<PageInfo> respDto = new RespDto<>(RespCodeEnum.SUCC.getCode(), RespCodeEnum.SUCC.getMsg(), pageInfo);
        return respDto;
    }

    @RequestMapping("/jobLogIndex")
    public String jobLogIndex() {
        return "jobLog";
    }

    @RequestMapping("/jobLogData")
    @ResponseBody
    public RespDto jobLogData(int page, long dagLogId) {
        PageInfo<JobTriggerLogDto> pageInfo = centerJobLogService.selectJobLogByPage(page, pageSize, dagLogId);
        RespDto<PageInfo> respDto = new RespDto<>(RespCodeEnum.SUCC.getCode(), RespCodeEnum.SUCC.getMsg(), pageInfo);
        return respDto;
    }
}
