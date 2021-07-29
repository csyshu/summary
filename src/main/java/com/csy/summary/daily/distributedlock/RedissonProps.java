package com.csy.summary.daily.distributedlock;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author csy
 * redisson配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "redisson")
public class RedissonProps {
    private String clusterNodes;
    private Integer expireSeconds;
    private Integer commandTimeout;
    private String password;
}
