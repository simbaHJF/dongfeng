package com.simba.dongfeng.center.controller;

import com.alibaba.fastjson.JSON;
import com.simba.dongfeng.center.enums.ExecutorRouterStgEnum;
import com.simba.dongfeng.center.pojo.DagDto;
import com.simba.dongfeng.center.pojo.JobDto;
import com.simba.dongfeng.center.service.CenterDagService;
import com.simba.dongfeng.center.service.CenterDependencyService;
import com.simba.dongfeng.center.service.CenterJobService;
import com.simba.dongfeng.common.builder.RespDtoBuilder;
import com.simba.dongfeng.common.enums.RespCodeEnum;
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
 * DATE:   2019/8/30 17:54
 * AUTHOR: simba.hjf
 * DESC:  管理后台crud相关操作controller
 **/
@Controller
public class DongfengCenterAdminController {

    @Resource
    private CenterDagService centerDagService;
    @Resource
    private CenterJobService centerJobService;
    @Resource
    private CenterDependencyService centerDependencyService;

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
        RespDto<List<DagDto>> respDto = new RespDto<>(RespCodeEnum.SUCC.getCode(), RespCodeEnum.SUCC.getMsg(), dagDtoList);
        return JSON.toJSONString(respDto);
    }

    @RequestMapping("/addDag")
    public String addDag(DagDto dagDto) {
        centerDagService.insertDag(dagDto);
        System.out.println(dagDto);
        return "dag";
    }

    @RequestMapping("/updateDagPage")
    public String updateDagPage(Model model, long dagId) {
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
        RespDto<List<JobDto>> respDto = new RespDto<>(RespCodeEnum.SUCC.getCode(), RespCodeEnum.SUCC.getMsg(), jobDtoList);
        return JSON.toJSONString(respDto);
    }

    @RequestMapping("/addJob")
    public String addJob(JobDto jobDto) {
        System.out.println(jobDto);
        centerJobService.insertJobAndDependency(jobDto);
        return "job";
    }

    @RequestMapping("/updateJobPage")
    public String updateJobPage(Model model, long jobId) {
        JobDto jobDto = centerJobService.selectJobById(jobId);
        List<Long> parentJobIds = Optional.ofNullable(centerDependencyService.selectParentJobIdList(jobId)).orElse(new ArrayList<>());
        jobDto.setParentJobIds(parentJobIds.stream().map(ele -> String.valueOf(ele)).collect(Collectors.joining(",")));
        System.out.println(jobDto);
        model.addAttribute("jobInfo", jobDto);
        return "updateJob";
    }

    @RequestMapping("/updateJobInfo")
    public String updateJobInfo(JobDto jobDto) {
        System.out.println(jobDto);
        if (StringUtils.isBlank(jobDto.getAssignIp())) {
            jobDto.setAssignIp("");
        }

        if (!StringUtils.equals(ExecutorRouterStgEnum.ASSIGN.getRouterName(), jobDto.getScheduleType())) {
            jobDto.setAssignIp("");
        }
        centerJobService.updateJob(jobDto);
        return "redirect:/jobIndex";
    }
}
