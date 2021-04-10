package com.csy.druid.demo.config.manualrocket;

import com.csy.druid.demo.component.rocket.RocketMsgListener;
import com.csy.druid.demo.config.properties.ConsumerConfigure;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author csy
 * description 注入消费者bean
 * @date create in 20:50 2019/10/11
 */
// @Configuration
@Slf4j
public class ConsumerConfig {
    @Resource
    private ConsumerConfigure consumerConfigure;
    @Resource
    private RocketMsgListener msgListener;

    @Bean("defaultMQPushConsumer")
    // @ConditionalOnProperty(prefix = "rocketmq.consumer",value = "default")
    public DefaultMQPushConsumer getRocketMQConsumer() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerConfigure.getGroupName());
        consumer.setNamesrvAddr(consumerConfigure.getNamesrvAddr());
        consumer.setConsumeThreadMin(consumerConfigure.getConsumeThreadMin());
        consumer.setConsumeThreadMax(consumerConfigure.getConsumeThreadMax());
        consumer.registerMessageListener(msgListener);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        consumer.setConsumeMessageBatchMaxSize(consumerConfigure.getConsumeMessageBatchMaxSize());
        try {
            String[] topicTagsArr = consumerConfigure.getTopics().split(";");
            for (String topicTags : topicTagsArr) {
                String[] topicTag = topicTags.split("~");
                consumer.subscribe(topicTag[0], topicTag[1]);
            }
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        return consumer;
    }

}