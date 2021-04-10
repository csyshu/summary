package com.csy.summary.daily.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author csy
 * description
 * @date create in 10:27 2019/10/23
 */
@Data
// @Component
// @ConfigurationProperties(prefix = "rocketmq.consumer")
public class ConsumerConfigure {
    private String isOnOff;
    private String groupName;
    private String namesrvAddr;
    private String topics;
    private Integer consumeThreadMin;
    private Integer consumeThreadMax;
    private Integer consumeMessageBatchMaxSize;
}
