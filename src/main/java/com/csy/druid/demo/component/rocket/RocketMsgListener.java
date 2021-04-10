package com.csy.druid.demo.component.rocket;

import com.csy.druid.demo.config.properties.OtherConfigure;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author csy
 * description
 * @date create in 20:51 2019/10/11
 */
@Slf4j
// @Component
public class RocketMsgListener implements MessageListenerConcurrently {
    @Resource
    private OtherConfigure otherConfigure;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) {
        if (CollectionUtils.isEmpty(list)) {
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        log.info("接收到的消息数量为：{}", list.size());
        MessageExt messageExt = list.get(0);

        log.info("接受到的消息为：" + new String(messageExt.getBody()));
        int reConsume = messageExt.getReconsumeTimes();
        // 消息已经重试了3次，如果不需要再次消费，则返回成功
        if (reConsume == 3) {
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        if (messageExt.getTopic().equals(otherConfigure.getTestTopic())) {
            String tags = messageExt.getTags();
            if ("rocketTestTag".equals(tags)) {
                log.info("匹配到了 tag == >>" + tags);
            } else {
                log.info("未匹配到 tag == >>" + tags);
            }
        }
        // 消息消费成功
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        //未消费成功，稍后重新消费
        // return ConsumeConcurrentlyStatus.RECONSUME_LATER;
    }

}
