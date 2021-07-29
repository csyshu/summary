package com.csy.summary.daily.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description：</p>
 *
 * @author shuyun.cheng
 * @date 2021/7/29 15:21
 */
@Data
@ConfigurationProperties(prefix = "zookeeper")
@Configuration
public class ZookeeperConfigure {
    private String connectString;
    private Integer maxRetries;
    private Integer baseSleepTimeMs;
}
