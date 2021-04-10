package com.csy.druid.demo.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author csy
 * description
 * @date create in 10:23 2019/10/23
 */
@Data
// @Component
// @ConfigurationProperties(prefix = "rocketmq.producer")
public class ProducerConfigure {
    private String isOnOff;
    private String groupName;
    private String namesrvAddr;
    private Integer maxMessageSize;
    private Integer sendMsgTimeout;
    private Integer retryTimesWhenSendFailed;
}
