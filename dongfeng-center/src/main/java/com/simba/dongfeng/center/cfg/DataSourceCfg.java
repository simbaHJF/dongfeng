package com.simba.dongfeng.center.cfg;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * DATE:   2019-08-14 14:59
 * AUTHOR: simba.hjf
 * DESC:
 **/
@Configuration
@PropertySource("classpath:db.properties")
@MapperScan(basePackages = {"com.simba.dongfeng.center.dao"},sqlSessionFactoryRef = "sqlSessionFactory",sqlSessionTemplateRef = "sqlSessionTemplate")
public class DataSourceCfg {

    @Value("dongfeng.datasource.druid.url")
    private String url;

    @Value("dongfeng.datasource.username")
    private String username;

    @Value("dongfeng.datasource.password")
    private String password;

    @Value("dongfeng.datasource.driver-class-name")
    private String driverClassName;

    @Value("dongfeng.datasource.druid.max-active")
    private int maxActive;

    @Value("dongfeng.datasource.druid.initial-size")
    private int initialSize;

    @Value("dongfeng.datasource.druid.min-idle")
    private int minIdle;

    @Value("dongfeng.datasource.druid.max-wait")
    private int maxWait;

    @Value("dongfeng.datasource.druid.time-between-eviction-runs-millis")
    private int timeBetweenEvictionRunsMillis;

    @Value("dongfeng.datasource.druid.min-evictable-idle-time-millis")
    private int minEvictableIdleTimeMillis;

    @Value("dongfeng.datasource.druid.validation-query")
    private String validationQuery;

    @Value("dongfeng.datasource.druid.test-while-idle")
    private boolean testWhileIdle;

    /**
     * DataSource
     * @return
     */
    @Bean(name = "dataSource")
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(url);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);
        datasource.setMaxActive(maxActive);
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        return datasource;
    }


    /**
     * 数据源事务管理器
     * @param dataSource
     * @return
     * @throws SQLException
     */
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager bossTransactionManager(@Qualifier("dataSource") DataSource dataSource) throws SQLException {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * Mybatis的连接会话工厂实例
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory bossSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        return sessionFactory.getObject();
    }

    /**
     * Mybatis的SqlSessionTemplate
     * @param sqlSessionFactory
     * @return
     * @throws Exception
     */
    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate bossSqlSessionTemplate(
            @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
