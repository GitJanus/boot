package com.example.boot.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringRedisQueueApplicationTests {

    @Autowired
    private MessageProducerService producer;

    @Autowired
    private MessageConsumerService consumer;

    @Test
    public void testQueue() {
        consumer.start();
        for(int i=0;i<100;i++){
            producer.sendMeassage(new MessageEntity(String.valueOf(i), String.valueOf(i)+1));
        }
        System.out.println("完成录入++++++++++++++++");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
         consumer.interrupt();
    }

}

