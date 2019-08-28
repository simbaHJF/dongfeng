package com.simba.dongfeng.center.controller;

import com.alibaba.fastjson.JSON;
import com.simba.dongfeng.center.converter.PojoConverter;
import com.simba.dongfeng.center.core.ScheduleCenter;
import com.simba.dongfeng.center.pojo.DagDto;
import com.simba.dongfeng.center.service.CenterExecutorService;
import com.simba.dongfeng.common.builder.RespDtoBuilder;
import com.simba.dongfeng.common.enums.RespCodeEnum;
import com.simba.dongfeng.common.pojo.Callback;
import com.simba.dongfeng.center.pojo.ExecutorDto;
import com.simba.dongfeng.common.pojo.ExecutorHeartbeatInfo;
import com.simba.dongfeng.common.pojo.RespDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
        List<DagDto> dagDtoList = new ArrayList<>();
        DagDto dagDto0 = new DagDto();
        dagDto0.setId(0);
        dagDto0.setDagName("dagDto0");
        dagDto0.setDagGroup("group");
        dagDto0.setDagCron("* * * * * *");
        dagDto0.setStatus(1);
        dagDto0.setParam("param0");

        DagDto dagDto1 = new DagDto();
        dagDto1.setId(1);
        dagDto1.setDagName("dagDto1");
        dagDto1.setDagGroup("group");
        dagDto1.setDagCron("* * * * * *");
        dagDto1.setStatus(2);
        dagDto1.setParam("param1");

        dagDtoList.add(dagDto0);
        dagDtoList.add(dagDto1);

        RespDto<List<DagDto>> respDto = new RespDto<>(RespCodeEnum.SUCC.getCode(),RespCodeEnum.SUCC.getMsg(),dagDtoList);

        return JSON.toJSONString(respDto);
    }

    @RequestMapping("/testPath2")
    public String test2() {
        return "test2";
    }
}
