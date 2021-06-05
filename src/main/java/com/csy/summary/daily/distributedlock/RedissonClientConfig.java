package com.csy.summary.daily.distributedlock;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author csy
 * description  分布式锁客户端配置类
 * @date create in 16:52 2019/12/20
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(RedissonProps.class)
public class RedissonClientConfig {

    @Resource
    private RedissonProps redissonProps;

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnProperty(name = "redisson.clusterNodes")
    public RedissonClient getRedisson() {
        String clusterNodes = this.redissonProps.getClusterNodes();
        String[] nodes = clusterNodes.split(",");
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = "redis://" + nodes[i];
        }
        Config config = new Config();
        //这是用的集群server
        config.useClusterServers()
                //设置集群状态扫描时间
                .setScanInterval(2000)
                .addNodeAddress(nodes)
                .setConnectTimeout(this.redissonProps.getCommandTimeout());
        RedissonClient redissonClient = Redisson.create(config);
        try {
            log.info("redissionClient load success,the config is {}", redissonClient.getConfig().toJSON());
        } catch (IOException e) {
            log.error("redissionClient config toJSON error:", e);
        }
        return redissonClient;
    }

}
