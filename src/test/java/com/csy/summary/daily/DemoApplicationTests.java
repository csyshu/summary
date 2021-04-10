package com.csy.summary.daily;

import com.csy.summary.daily.beans.Contract;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
    @Autowired
    private DefaultMQProducer defaultMQProducer;
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Test
    public void contextLoads() {
        try {
            Message message = new Message();
            message.setTopic("rocketTestTopic");
            message.setTags("rocketTestTag");
            message.setBody("rocketmq-test".getBytes(StandardCharsets.UTF_8));
            defaultMQProducer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("send success result:" + sendResult);
                }

                @Override
                public void onException(Throwable throwable) {
                    System.out.println("send error e:" + throwable);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            defaultMQProducer.shutdown();
        }
    }

    @Test
    public void testSyncSend() {
        SendResult sendResult = rocketMQTemplate.syncSend("rocketTestTopic", "dddddd");
        System.out.println(sendResult);
    }

    @Test
    public void test() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer container = kieServices.getKieClasspathContainer();
        KieSession statefulKieSession = container.newKieSession("myAgeSession");
        Contract contract = new Contract();
        contract.setIsTest(1);
        statefulKieSession.insert(contract);
        statefulKieSession.fireAllRules();
        statefulKieSession.dispose();
    }

}
