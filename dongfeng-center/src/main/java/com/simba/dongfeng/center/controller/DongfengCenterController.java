package com.simba.dongfeng.center.controller;

import com.alibaba.fastjson.JSON;
import com.simba.dongfeng.center.converter.PojoConverter;
import com.simba.dongfeng.center.core.ScheduleCenter;
import com.simba.dongfeng.center.pojo.DagDto;
import com.simba.dongfeng.center.pojo.DependencyDto;
import com.simba.dongfeng.center.pojo.ExecutorDto;
import com.simba.dongfeng.center.pojo.JobDto;
import com.simba.dongfeng.center.service.CenterDagService;
import com.simba.dongfeng.center.service.CenterDependencyService;
import com.simba.dongfeng.center.service.CenterExecutorService;
import com.simba.dongfeng.center.service.CenterJobService;
import com.simba.dongfeng.common.builder.RespDtoBuilder;
import com.simba.dongfeng.common.enums.RespCodeEnum;
import com.simba.dongfeng.common.pojo.Callback;
import com.simba.dongfeng.common.pojo.ExecutorHeartbeatInfo;
import com.simba.dongfeng.common.pojo.RespDto;
import org.apache.commons.lang3.StringUtils;
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
 * DATE:   2019-08-14 17:12
 * AUTHOR: simba.hjf
 * DESC:
 **/

@Controller
public class DongfengCenterController {
    @Resource
    private CenterExecutorService centerExecutorService;
    @Resource
    private CenterDagService centerDagService;
    @Resource
    private CenterJobService centerJobService;
    @Resource
    private CenterDependencyService centerDependencyService;
    @Resource
    private ScheduleCenter scheduleCenter;

    @RequestMapping("/executor/heartbeat")
    @ResponseBody
    public RespDto heartbeat(ExecutorHeartbeatInfo executorHeartbeatInfo) {
        if (executorHeartbeatInfo == null || StringUtils.isBlank(executorHeartbeatInfo.getExecutorName()) ||
                StringUtils.isBlank(executorHeartbeatInfo.getExecutorIp()) ||
                StringUtils.isBlank(executorHeartbeatInfo.getExecutorPort()) ||
                StringUtils.isBlank(executorHeartbeatInfo.getExecutorGroup())) {
            return RespDtoBuilder.createBuilder().badReqResp().build();
        }
        try {
            ExecutorDto executorDto = PojoConverter.convertExecutorHeartbeatInfo(executorHeartbeatInfo);
            centerExecutorService.replaceExecutor(executorDto);
            return RespDtoBuilder.createBuilder().succResp().build();
        } catch (Exception e) {
            return RespDtoBuilder.createBuilder().serverErrResp().build();
        }
    }

    @RequestMapping("/callback")
    @ResponseBody
    public RespDto callback(Callback callback) {
        if (callback == null) {
            return RespDtoBuilder.createBuilder().badReqResp().build();
        }
        scheduleCenter.callback(callback);
        return RespDtoBuilder.createBuilder().succResp().build();
    }

    @RequestMapping("/manualTrigger")
    @ResponseBody
    public RespDto manualTrigger(long dagId, String param) {
        if (dagId == 0) {
            return RespDtoBuilder.createBuilder().badReqResp().build();
        }
        scheduleCenter.manualTrigger(dagId, param);
        return RespDtoBuilder.createBuilder().succResp().build();
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
    public String dagData() {
        List<DagDto> dagDtoList = Optional.ofNullable(centerDagService.selectAllDag())
                .orElse(new ArrayList<>());
        RespDto<List<DagDto>> respDto = new RespDto<>(RespCodeEnum.SUCC.getCode(),RespCodeEnum.SUCC.getMsg(),dagDtoList);
        return JSON.toJSONString(respDto);
    }

    @RequestMapping("/addDag")
    public String addDag(DagDto dagDto) {
        centerDagService.insertDag(dagDto);
        System.out.println(dagDto);
        return "dag";
    }

    @RequestMapping("/updatePage")
    public String updatePage(Model model,long dagId) {
        DagDto dagDto = centerDagService.selectDagById(dagId);
        model.addAttribute("dagInfo", dagDto);
        return "updateDag";
    }


    @RequestMapping("/updateDagInfo")
    public String updateDagInfo(DagDto dagDto) {
        System.out.println(dagDto);
        centerDagService.updateDagInfo(dagDto);
        return "redirect:/dagIndex";
    }

    @RequestMapping("/deleteDagInfo")
    @ResponseBody
    public RespDto deleteDagInfo(@RequestParam("dagId") long dagId) {
        System.out.println(dagId);
        centerDagService.deleteDagInfo(dagId);
        return RespDtoBuilder.createBuilder().succResp().build();
    }

    @RequestMapping("/jobIndex")
    public String jobIndex() {
        return "job";
    }

    @RequestMapping("/jobData")
    @ResponseBody
    public String jobData() {
        List<JobDto> jobDtoList = Optional.ofNullable(centerJobService.selectAllJob())
                .orElse(new ArrayList<>());
        for (JobDto jobDto : jobDtoList) {
            List<Long> parentJobIds = Optional.ofNullable(centerDependencyService.selectParentJobIdList(jobDto.getId())).orElse(new ArrayList<>());

            jobDto.setParentJobIds(parentJobIds.stream().map(ele -> String.valueOf(ele)).collect(Collectors.joining(",")));
        }
        RespDto<List<JobDto>> respDto = new RespDto<>(RespCodeEnum.SUCC.getCode(),RespCodeEnum.SUCC.getMsg(),jobDtoList);
        return JSON.toJSONString(respDto);
    }

    @RequestMapping("/addJob")
    public String addJob(JobDto jobDto) {

        return "job";
    }
}
