package com.csy.druid.demo.config.manualrocket;

import com.csy.druid.demo.config.properties.ProducerConfigure;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author csy
 * description producer类的创建类，需要注意的是这个producer一个程序里面只能出现一个，当重复创建时就会报错
 * @date create in 20:49 2019/10/11
 */
// @Configuration
@Slf4j
public class ProducerConfig {
    @Resource
    private ProducerConfigure producerConfigure;

    @Bean("defaultMQProducer")
    // @ConditionalOnProperty(prefix = "rocketmq.producer", value = "default", havingValue = "true")
    public DefaultMQProducer getRocketMQProducer() {
        DefaultMQProducer producer;
        producer = new DefaultMQProducer(producerConfigure.getGroupName());
        producer.setNamesrvAddr(producerConfigure.getNamesrvAddr());
        //如果需要同一个jvm中不同的producer往不同的mq集群发送消息，需要设置不同的instanceName
        if (producerConfigure.getMaxMessageSize() != null) {
            producer.setMaxMessageSize(producerConfigure.getMaxMessageSize());
        }
        if (producerConfigure.getSendMsgTimeout() != null) {
            producer.setSendMsgTimeout(producerConfigure.getSendMsgTimeout());
        }
        //如果发送消息失败，设置重试次数，默认为2次
        if (producerConfigure.getRetryTimesWhenSendFailed() != null) {
            producer.setRetryTimesWhenSendFailed(producerConfigure.getRetryTimesWhenSendFailed());
        }
        //如果使用注解注入，就不用主动调用start，否则会报：MQClientException: The producer service state not OK, maybe started once, RUNNING
        // try {
        //     producer.start();
        //     log.info("rocketmq producer server开启成功---------------------------------.");
        // } catch (MQClientException e) {
        //     e.printStackTrace();
        //     log.error("rocketmq producer server开启异常.", e);
        // }
        return producer;
    }

    /**
     * 创建事务消息发送者实例
     *
     * @return
     * @throws MQClientException
     */
    // @Bean
    // // @ConditionalOnProperty(prefix = "rocketmq.producer", value = "transaction", havingValue = "true")
    // public TransactionMQProducer transactionMQProducer() throws MQClientException {
    //     log.info(producerConfigure.toString());
    //     log.info("TransactionMQProducer 正在创建---------------------------------------");
    //     TransactionMQProducer producer = new TransactionMQProducer(producerConfigure.getGroupName());
    //
    //     ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS,
    //             new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
    //         @Override
    //         public Thread newThread(Runnable r) {
    //             Thread thread = new Thread(r);
    //             thread.setName("client-transaction-msg-check-thread");
    //             return thread;
    //         }
    //     });
    //     producer.setNamesrvAddr(producerConfigure.getNamesrvAddr());
    //     producer.setExecutorService(executorService);
    //     producer.start();
    //     log.info("TransactionMQProducer server开启成功---------------------------------.");
    //     return producer;
    // }

}
