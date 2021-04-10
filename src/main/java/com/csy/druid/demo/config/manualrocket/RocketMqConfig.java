package com.csy.druid.demo.config.manualrocket;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

/**
 * <p>Description：</p>
 *
 * @author shuyun.cheng
 * @date 2020/8/25 15:20
 */
@Slf4j
// @Configuration
public class RocketMqConfig {

    @Bean("rocketMQTemplate")
    public RocketMQTemplate getRocketMqTemplate(@Qualifier("defaultMQProducer") DefaultMQProducer defaultMQProducer) {
        RocketMQTemplate rocketMqTemplate = new RocketMQTemplate();
        rocketMqTemplate.setCharset(StandardCharsets.UTF_8.name());
        rocketMqTemplate.setProducer(defaultMQProducer);
        return rocketMqTemplate;
    }
}
