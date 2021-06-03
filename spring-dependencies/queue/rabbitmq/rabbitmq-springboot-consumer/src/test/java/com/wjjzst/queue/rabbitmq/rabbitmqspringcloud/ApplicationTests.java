package com.wjjzst.queue.rabbitmq.rabbitmqspringcloud;

import com.wjjzst.queue.rabbitmq.rabbitmqcommon.entity.Order;
import com.wjjzst.queue.rabbitmq.rabbitmqspringcloud.producer.RabbitSend;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {
    @Test
    public void contextLoads() {

    }

    @Autowired
    private RabbitSend rabbitSend;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Test
    public void testSend1() {
        Map<String, Object> prperties = new HashMap<>();
        prperties.put("number", "12345");
        prperties.put("send_time", simpleDateFormat.format(new Date()));
        rabbitSend.send("Hello RabbitMQ For Springboot", prperties);
        // 睡2秒ack即为true
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSend2() {

        rabbitSend.sendOrder(new Order("001","第一个订单"));
        // 睡2秒ack即为true
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
