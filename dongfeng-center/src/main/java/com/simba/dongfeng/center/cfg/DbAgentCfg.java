package com.simba.dongfeng.center.cfg;

import cn.techwolf.dbwolf.client.DbAgent;
import com.bosszhipin.common.ParamDbWolfConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * DATE:   2019/9/10 14:53
 * AUTHOR: simba.hjf
 * DESC:
 **/
@Configuration
public class DbAgentCfg {

    @Value("${jade.db.zookeeper.host}")
    private String zkHost;
    @Value("${jade.db.zookeeper.path}")
    private String zkPath;

    @Bean(name = "dbAgent")
    public DbAgent dbAgent() {
        ParamDbWolfConfig.createDbWolfConfg(zkHost,zkPath);
        DbAgent dbAgent = new DbAgent(ParamDbWolfConfig.getInstance());
        return dbAgent;
    }
}
