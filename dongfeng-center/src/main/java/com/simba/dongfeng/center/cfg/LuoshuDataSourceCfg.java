package com.simba.dongfeng.center.cfg;

import cn.techwolf.dbwolf.client.DbAgent;
import com.bosszhipin.common.Utils;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DATE:   2019/9/10 15:23
 * AUTHOR: simba.hjf
 * DESC:
 **/
@Configuration
@MapperScan(basePackages = {"com.simba.dongfeng.center.dao"},sqlSessionTemplateRef = "sqlSessionTemplate")
public class LuoshuDataSourceCfg {
    /**
     * luoshu数据源
     *
     * @param dbAgent
     * @return
     */
    @Bean(name = "dataSource")
    public DataSource luoshuDataSource(@Qualifier("dbAgent") DbAgent dbAgent) {
        return Utils.getDsFromZk("rcd_luoshu", dbAgent, true);
    }

    /**
     * luoshu数据源事务管理器
     * @param dataSource
     * @return
     * @throws SQLException
     */
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager luoshuTransactionManager(@Qualifier("dataSource") DataSource dataSource) throws SQLException {
        return new DataSourceTransactionManager(dataSource);
    }


    @Bean(name = "pageInterceptor")
    public PageInterceptor pageInterceptor() {
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");

        pageInterceptor.setProperties(properties);
        return pageInterceptor;

    }

    /**
     * luoshu Mybatis的连接会话工厂实例
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory luoshuSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource,@Qualifier("pageInterceptor")PageInterceptor pageInterceptor) throws Exception {

        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        sessionFactory.setPlugins(pageInterceptor);
        return sessionFactory.getObject();
    }

    /**
     * luoshu Mybatis的SqlSessionTemplate
     * @param sqlSessionFactory
     * @return
     * @throws Exception
     */
    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate luoshuSqlSessionTemplate(
            @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}