package com.csy.summary.daily.elasticjob;

import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shuyun.cheng
 */
@Configuration
public class ElasticJobRegistryCenterConfig {

    /**
     * zookeeper链接字符串 localhost:2181
     */
    private static final String ZOOKEEPER_CONNECTION_STRING = "localhost:2181";
    /**
     * 定时任务命名空间
     */
    private static final String JOB_NAMESPACE = "elastic-job-boot-java";

    /**
     * zk的配置及创建注册中心
     *
     * @return CoordinatorRegistryCenter
     */
    @Bean(initMethod = "init")
    public CoordinatorRegistryCenter setUpAndGetRegistryCenter() {
        //zk的配置
        ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration(ZOOKEEPER_CONNECTION_STRING, JOB_NAMESPACE);
        zookeeperConfiguration.setSessionTimeoutMilliseconds(1000);
        //创建注册中心
        return new ZookeeperRegistryCenter(zookeeperConfiguration);

    }
}