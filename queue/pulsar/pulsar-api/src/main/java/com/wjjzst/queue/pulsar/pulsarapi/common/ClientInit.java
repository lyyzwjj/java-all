package com.wjjzst.queue.pulsar.pulsarapi.common;

import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;

import java.util.concurrent.TimeUnit;

public class ClientInit {
    private static final String IP = "localhost";

    /**
     * 新建key-value客户端实例
     *
     * @return
     */
    public PulsarClient getClient() {
        String endpoints = "pulsar://" + IP + ":6650," + IP + ":6651," + IP + ":6652";
        PulsarClient client;
        try {
//        client = PulsarClient.builder()
//                .serviceUrl(endpoints)
//                .build();
            //构造Pulsar client
            client = PulsarClient.builder()
                    .serviceUrl(endpoints)
                    .operationTimeout(30000, TimeUnit.MILLISECONDS) //操作超时时长，默认30000ms
                    .ioThreads(1) //用于处理到brokers的连接的线程数
                    .listenerThreads(1) //用于处理消息监听器的线程数。如果您希望多个线程处理单个主题，则需要创建一个shared订阅，并为此订阅创建多个消费者。这并不能确保排序。
                    .maxConcurrentLookupRequests(5000) //允许在每个broker连接上发送的并发查找请求数，以防止broker过载
                    .maxLookupRequests(50000) //每个broker连接上允许的最大查找请求数，以防止broker过载
                    .keepAliveInterval(30, TimeUnit.SECONDS) //每个客户端broker连接的保持活跃间隔的秒数，默认30s
                    .connectionTimeout(10000, TimeUnit.MILLISECONDS) //等待建立连接到broker的持续时间，如果持续时间过了，没有从broker得到响应，连接尝试将被放弃
                    .build();
        } catch (PulsarClientException e) {
            e.printStackTrace();
            System.out.println("Init PulsarClient Failed !!!");
            throw new RuntimeException("");
        }
        return client;
    }
}
