package com.csy.summary.daily.component.datasource;


import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author csy
 * description
 * @date create in 14:14 2019/11/11
 */
@Configuration
@MapperScan(value = "com.csy.summary.daily.mapper", sqlSessionFactoryRef = "myMpSqlSessionFactory")
public class MybatisPlusConfig {
    @Resource
    private MybatisPlusProperties properties;

    @Autowired(required = false)
    private Interceptor[] interceptors;

    @Autowired(required = false)
    private DatabaseIdProvider databaseIdProvider;

    /**
     * mybatis-plus分页插件
     */
    @Bean(name = "myPaginationInterceptor")
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Resource
    private DataSource myDataSource;

    @Bean(name = "myMpSqlSessionFactory")
    public MybatisSqlSessionFactoryBean getSqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean mybatisPlus = new MybatisSqlSessionFactoryBean();
        mybatisPlus.setDataSource(myDataSource);
        //DefaultVFS在获取jar上存在问题，使用springbootVFS.addImplClass(SpringBootVFS.class);
        //SpringBoot完整包部署，找不到AccountStatistis，手动设置SpringBootVFS
        mybatisPlus.setVfs(SpringBootVFS.class);
        // 设置分页插件
        mybatisPlus.setPlugins(this.interceptors);
        MybatisConfiguration mc = new MybatisConfiguration();
        mc.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        // 数据库和java都是驼峰，就不需要
        mc.setMapUnderscoreToCamelCase(true);
        mybatisPlus.setConfiguration(mc);
        // 开发环境打印sql
        // if (!env.getProperty("apibackend.env").equals(SysConstant.SYSTEM_ENV_PROD)) {
        //     mc.setLogImpl(StdOutImpl.class);
        // }
        if (this.databaseIdProvider != null) {
            mybatisPlus.setDatabaseIdProvider(this.databaseIdProvider);
        }
        mybatisPlus.setTypeAliasesPackage("com.csy.summary.daily.beans");
        mybatisPlus.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
        mybatisPlus.setMapperLocations(this.properties.resolveMapperLocations());
        // 设置mapper.xml文件的路径
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        org.springframework.core.io.Resource[] resources = resolver.getResources("classpath:mapper/**.xml");
        mybatisPlus.setMapperLocations(resources);

        return mybatisPlus;
    }

}
