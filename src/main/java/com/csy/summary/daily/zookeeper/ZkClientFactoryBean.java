package com.csy.summary.daily.zookeeper;

import com.csy.summary.daily.config.properties.ZookeeperConfigure;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author shuyun.cheng
 */
//@Component
public class ZkClientFactoryBean implements FactoryBean<CuratorFramework> {
    @Autowired
    private ZookeeperConfigure zookeeperConfigure;

    private CuratorFramework curatorClient;

    @PostConstruct
    public void init() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(zookeeperConfigure.getBaseSleepTimeMs(), zookeeperConfigure.getMaxRetries());
        curatorClient = CuratorFrameworkFactory.builder()
                .connectString(zookeeperConfigure.getConnectString())
                .retryPolicy(retryPolicy)
                .build();
        curatorClient.start();
    }

    @Override
    public CuratorFramework getObject() throws Exception {
        return curatorClient;
    }

    @Override
    public Class<?> getObjectType() {
        return CuratorFramework.class;
    }
}