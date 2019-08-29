package com.simba.dongfeng.center.enums;

import com.simba.dongfeng.center.core.route.ExecutorRouterStg;
import com.simba.dongfeng.center.core.route.impl.ExecutorRouterAssignStg;
import com.simba.dongfeng.center.core.route.impl.ExecutorRouterRandomStg;

import java.util.stream.Stream;

/**
 * DATE:   2019-08-16 14:06
 * AUTHOR: simba.hjf
 * DESC:    执行器路由策略枚举
 **/
public enum ExecutorRouterStgEnum {
    RANDOM("random", new ExecutorRouterRandomStg()),
    ASSIGN("assign", new ExecutorRouterAssignStg());
    private String routerName;
    private ExecutorRouterStg executorRouterStg;

    ExecutorRouterStgEnum(String routerName, ExecutorRouterStg executorRouterStg) {
        this.routerName = routerName;
        this.executorRouterStg = executorRouterStg;
    }

    public String getRouterName() {
        return routerName;
    }

    public ExecutorRouterStg getExecutorRouterStg() {
        return executorRouterStg;
    }

    public static ExecutorRouterStgEnum getExecutorRouterStgEnum(String scheduleType) {
        return Stream.of(ExecutorRouterStgEnum.values())
                .filter(ele -> ele.getRouterName().equals(scheduleType))
                .findFirst()
                .orElse(null);
    }
}
