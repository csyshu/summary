package com.csy.summary.daily.component.datasource;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author csy
 * description 数据库配置类
 * @date 2018/6/6 10:39
 */
@Configuration
@MapperScan(basePackages = "com.csy.summary.daily.mapper", sqlSessionTemplateRef = "mySqlSessionTemplate")
public class DataSourceConfig {

    @Autowired
    private Environment env;

    @Bean(name = "myDataSource")
    @Primary
    public DataSource getDataSource() throws Exception {
        Properties props = new Properties();
        props.put("driverClassName", env.getProperty("spring.datasource.driver-class-name"));
        props.put("url", env.getProperty("spring.datasource.url"));
        props.put("username", env.getProperty("spring.datasource.username"));
        props.put("password", env.getProperty("spring.datasource.password"));
        props.put("initialSize", env.getProperty("spring.datasource.druid.initial-size"));
        props.put("minIdle", env.getProperty("spring.datasource.druid.min-idle"));
        props.put("maxActive", env.getProperty("spring.datasource.druid.max-active"));
        props.put("maxWait", env.getProperty("spring.datasource.druid.max-wait"));
        props.put("timeBetweenEvictionRunsMillis", env.getProperty("spring.datasource.druid.time-between-eviction-runs-millis"));
        props.put("minEvictableIdleTimeMillis", env.getProperty("spring.datasource.druid.min-evictable-idle-time-millis"));
        props.put("validationQuery", env.getProperty("spring.datasource.druid.validation-query"));
        props.put("testWhileIdle", env.getProperty("spring.datasource.druid.test-while-idle"));
        props.put("testOnBorrow", env.getProperty("spring.datasource.druid.test-on-borrow"));
        props.put("testOnReturn", env.getProperty("spring.datasource.druid.test-on-return"));
        props.put("poolPreparedStatements", env.getProperty("spring.datasource.druid.pool-prepared-statements"));
        props.put("maxPoolPreparedStatementPerConnectionSize", env.getProperty("spring.datasource.druid.max-pool-prepared-statement-per-connection-size"));
        props.put("filters", env.getProperty("spring.datasource.druid.filters"));
        props.put("logAbandoned", env.getProperty("spring.datasource.druid.log-abandoned"));
        return DruidDataSourceFactory.createDataSource(props);
    }

    @Bean(name = "mySqlSessionFactory")
    @Primary
    public SqlSessionFactory getSqlSessionFactory(@Qualifier("myDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        return bean.getObject();
    }

    @Bean(name = "myTransactionManager")
    @Primary
    public DataSourceTransactionManager getTransactionManager(@Qualifier("myDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "mySqlSessionTemplate")
    @Primary
    public SqlSessionTemplate getSqlSessionTemplate(@Qualifier("mySqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
