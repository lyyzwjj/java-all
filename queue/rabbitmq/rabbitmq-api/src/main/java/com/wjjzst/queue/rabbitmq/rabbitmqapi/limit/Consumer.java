package com.wjjzst.queue.rabbitmq.rabbitmqapi.limit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: Wjj
 * @Date: 2019/8/22 1:03
 * @desc:
 */
public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 1 创建连接工厂ConnectionFactory 并进行配置
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("129.204.35.106");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("wjj");
        connectionFactory.setPassword("wzzst310");

        // 2 通过连接工厂创建一个连接
        Connection connection = connectionFactory.newConnection();
        // 3 通过connection创建一个channel
        Channel channel = connection.createChannel();
        // 4 声明交换机和队列   然后进行绑定设置    最后指定路由Key
        String exchangeName = "test_qos_exchange";
        String routingKey = "qos.#";
        String queueName = "test_qos_queue";
        channel.exchangeDeclare(exchangeName, "topic", true, false, null);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);
        // 5 设置Channel
        // 限流方式 第一件事情autoAck设置为false 手工确认签收
        channel.basicQos(0, 1, false);
        channel.basicConsume(queueName, false, new MyConsumer(channel));
        // 6 记得关闭相关的连接
        // channel.close();
        // connection.close();
    }
}
