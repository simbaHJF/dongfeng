package com.simba.dongfeng.center.core;

import com.simba.dongfeng.center.enums.DagTriggerTypeEnum;
import com.simba.dongfeng.center.thread.CallbackHandleHelper;
import com.simba.dongfeng.center.thread.DagFetchHelper;
import com.simba.dongfeng.center.thread.DagScheduleHelper;
import com.simba.dongfeng.center.thread.ExpireLogClearHelper;
import com.simba.dongfeng.common.pojo.Callback;
import com.simba.dongfeng.center.pojo.DagDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * DATE:   2019-08-15 15:50
 * AUTHOR: simba.hjf
 * DESC:    调度中心
 **/
@Service
public class ScheduleCenter {
    private Logger logger = LoggerFactory.getLogger(ScheduleCenter.class);

    private DagQueue dagQueue = new DagQueue();
    private CallbackQueue callbackQueue = new CallbackQueue();

    private DagFetchHelper dagFetchHelper;
    private DagScheduleHelper dagScheduleHelper;
    private CallbackHandleHelper callbackHandleHelper;
    private ExpireLogClearHelper expireLogClearHelper;


    @Resource
    private ScheduleServiceFacade scheduleServiceFacade;


    @PostConstruct
    public void init() {
        dagFetchHelper = new DagFetchHelper(dagQueue, scheduleServiceFacade);
        dagScheduleHelper = new DagScheduleHelper(dagQueue, scheduleServiceFacade);
        callbackHandleHelper = new CallbackHandleHelper(callbackQueue, scheduleServiceFacade);
        expireLogClearHelper = new ExpireLogClearHelper(scheduleServiceFacade);
        dagFetchHelper.start();
        dagScheduleHelper.start();
        callbackHandleHelper.start();
        expireLogClearHelper.start();
    }

    @PreDestroy
    public void destroy() {
        if (dagFetchHelper != null) {
            dagFetchHelper.stop();
        }
        if (dagScheduleHelper != null) {
            dagScheduleHelper.stop();
        }
        if (callbackHandleHelper != null) {
            callbackHandleHelper.stop();
        }
        if (expireLogClearHelper != null) {
            expireLogClearHelper.stop();
        }
    }

    public void callback(Callback callBack) {
        callbackQueue.addTail(callBack);
    }

    public void manualTrigger(long dagId, String param) {
        DagDto dag = scheduleServiceFacade.selectDagById(dagId);
        dag.setParam(param);
        dag.setTriggerType(DagTriggerTypeEnum.MANUAL.getValue());
        dagQueue.addHead(dag);
    }

}
