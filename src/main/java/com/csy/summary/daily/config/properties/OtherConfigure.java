package com.csy.summary.daily.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author csy
 * description
 * @date create in 10:28 2019/10/23
 */
@Data
// @Component
// @ConfigurationProperties(prefix = "rocketmq.other")
public class OtherConfigure {
    private String testGroup;
    private String testTopic;
    private String testTag;
}
