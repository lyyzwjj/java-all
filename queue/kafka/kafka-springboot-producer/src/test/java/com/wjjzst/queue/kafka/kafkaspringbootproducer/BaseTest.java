package com.wjjzst.queue.kafka.kafkaspringbootproducer;


import com.wjjzst.common.dtos.OrderDTO;
import com.wjjzst.queue.kafka.kafkaspringbootproducer.producer.KafkaProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/**
 * @author: Wjj
 * @create: 2020/8/21 4:31 下午
 * @Description
 */
@SpringBootTest
public class BaseTest {
    @Autowired
    private KafkaProducer producer;

    @Test
    public void produce() {
        while (true) {
            int num = 1000;
            for (int i = 0; i < num; i++) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                OrderDTO order = new OrderDTO();
                producer.send(order);
            }
        }

    }
}
