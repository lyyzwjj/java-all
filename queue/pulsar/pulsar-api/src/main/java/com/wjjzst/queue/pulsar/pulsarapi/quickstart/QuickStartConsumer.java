package com.wjjzst.queue.pulsar.pulsarapi.quickstart;


import com.wjjzst.queue.pulsar.pulsarapi.common.ClientInit;
import org.apache.pulsar.client.api.*;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class QuickStartConsumer {

    public static class ReceiveMessageFromConsumerHelper {
        private void receiveMessageFromConsumer(Consumer<byte[]> consumer) {
            consumer.receiveAsync().thenAccept(message -> {
                // Do something with the received message
                receiveMessageFromConsumer(consumer);
            });
        }
    }

    public static void main(String[] args) throws PulsarClientException {
        PulsarClient client = new ClientInit().getClient();
        String topicName = "quickstart";
        String subscriptionName = "my-subscription";
        // PulsarClient通过指定主题和订阅名称就能创建一个消费者
//        Consumer<byte[]> consumer = client.newConsumer()
//                .topic(topicName)
//                .subscriptionName(subscriptionName)
//                .subscribe();
        // 创建消费者的其他常用参数：
//        Consumer<byte[]> consumer = client.newConsumer()
//                .topic(topicName) //主题名称
//                .subscriptionName(subscriptionName) //订阅名称
//                .subscriptionType(SubscriptionType.Exclusive)//指定消费模式，包含：Exclusive，Failover，Shared，Key_Shared。默认Exclusive模式
//                .ackTimeout(10, TimeUnit.MILLISECONDS) //未确认消息的超时时间，默认值 0
//                .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)//指定从哪里开始消费还有Latest，valueof可选，默认Latest
//                .negativeAckRedeliveryDelay(60, TimeUnit.SECONDS)//指定消费失败后延迟多久broker重新发送消息给consumer，默认60s
//                .subscribe();
        // 接收消息并确认
        // 同步接收
        // 采用主线程阻塞方式
        /*while (true) {
            // Wait for a message
            Message<byte[]> msg = consumer.receive();
            try {
                // Do something with the message
                System.out.println("Message received: " + new String(msg.getData()));
                // Acknowledge the message so that it can be deleted by the message broker
                consumer.acknowledge(msg);
            } catch (Exception e) {
                // Message failed to process, redeliver later
                consumer.negativeAcknowledge(msg);
            }
        }*/
        // 如果你不想阻塞主线程，不断地监听新消息，请考虑使用 MessageListener 。
        MessageListener<byte[]> myMessageListener = (consumer, msg) -> {
            try {
                System.out.println("Message received: " + new String(msg.getData()));
                consumer.acknowledge(msg);
            } catch (Exception e) {
                consumer.negativeAcknowledge(msg);
            }
        };
        Consumer<byte[]> consumer = client.newConsumer()
                .topic(topicName)
                .subscriptionName(subscriptionName)
                .messageListener(myMessageListener)
                .subscribe();
        // 异步接收
        CompletableFuture<Message<byte[]>> asyncMessage = consumer.receiveAsync();
        asyncMessage.handle((v, ex) -> {
            if (ex == null) {
                System.out.printf("接收Pulsar消息成功: %s\n", asyncMessage);
            } else {
                System.err.printf("接收Pulsar消息失败msg:【%s】ex:【%s】\n", asyncMessage, ex);
            }
            return null;
        });
        // 一旦有新消息可用，它会立即返回一个 CompletableFuture 对象。
        // 异步接收操作返回包装在 CompletableFuture 中的 Message 。

        // 批量接收
        Messages<byte[]> messages = consumer.batchReceive();
        for (Message<byte[]> message : messages) {
            // do something
            System.out.println(new String(message.getData()));
        }
        consumer.acknowledge(messages);
        // 批量接收通过如下三个参数中任一个可控制：足够的消息数量、消息字节数、等待超时
        Consumer<byte[]> batchConsumer = client.newConsumer()
                .topic(topicName)
                .subscriptionName(subscriptionName)
                .batchReceivePolicy(BatchReceivePolicy.builder()        //设置批量接收策略
                        .maxNumMessages(100)                            // 足够的消息数量
                        .maxNumBytes(1024 * 1024)                       // 消息字节数
                        .timeout(200, TimeUnit.MILLISECONDS)    // 等待超时
                        .build())
                .subscribe();
        // 订阅方式说明
        // 通过namaspace或者正则表达式来实现多个主题订阅，也可直接指定主题列表（可跨命名空间）
        // Subscribe to all topics in a namespace
        Pattern allTopicsInNamespace = Pattern.compile("public/default/.*");
        Consumer<byte[]> allTopicsConsumer = client.newConsumer()
                .subscriptionName(subscriptionName)
                .topicsPattern(allTopicsInNamespace)
                .subscribe();
        // Subscribe to a subsets of topics in a namespace, based on regex
        Pattern someTopicsInNamespace = Pattern.compile("public/default/foo.*");
        Consumer someTopicsConsumer = client.newConsumer()
                .subscriptionName(subscriptionName)
                .topicsPattern(someTopicsInNamespace)
                .subscribe();
        // 多主题订阅默认是，消费者的 subscriptionTopicsMode 是 PersistentOnly 。 subscriptionTopicsMode 的可用选项有 PersistentOnly ， NonPersistentOnly 和 AllTopics 。
        // .subscriptionTopicsMode(RegexSubscriptionMode.AllTopics) //控制订阅的主题类型（持久化/非持久化/All）


        // 还可以异步订阅多个主题
        Consumer multiTopicConsumer = client.newConsumer()
                .topic(
                        "topic-1",
                        "topic-2",
                        "topic-3"
                )       //直接指定主题列表
                .subscribe();

        Pattern allTopicsInNamespace1 = Pattern.compile("persistent://public/default.*");
        client.newConsumer()
                .topics(Arrays.asList("topic-1", "topic-2", "topic-3"))
                .subscribeAsync()
                .thenAccept(c -> new ReceiveMessageFromConsumerHelper().receiveMessageFromConsumer(c));


        // 4种订阅模式
        // 一个主题可以有多个 不同订阅模式 的订阅。但是，一个subscription一次只能有一种订阅模式。除非此subscription的所有现有消费者都处于离线状态，否则你无法更改订阅模式。

        // 独占
        Consumer exclusiveConsumer = client.newConsumer()
                .topic("my-topic")
                .subscriptionName("my-subscription")
                .subscriptionType(SubscriptionType.Exclusive)
                .subscribe();
        // 若是分区topic，第一个消费者消费所有分区topic。消费顺序与生产顺序相同。

        // 灾备
        Consumer consumer1 = client.newConsumer()
                .topic("my-topic")
                .subscriptionName("my-subscription")
                .subscriptionType(SubscriptionType.Failover)
                .subscribe();
        Consumer consumer2 = client.newConsumer()
                .topic("my-topic")
                .subscriptionName("my-subscription")
                .subscriptionType(SubscriptionType.Failover)
                .subscribe();
        //conumser1 is the active consumer, consumer2 is the standby consumer.
        //consumer1 receives 5 messages and then crashes, consumer2 takes over as an  active consumer.
        // 多个消费者可以附加到同一个订阅，但只有第一个消费者是活跃的，其他消费者是备用的。
        // 共享
        // 如果一个主题是分区主题，则每个分区只有一个活跃消费者，一个分区的消息只分发给一个消费者，多个分区的消息分发给多个消费者。
        Consumer consumer11 = client.newConsumer()
                .topic("my-topic")
                .subscriptionName("my-subscription")
                .subscriptionType(SubscriptionType.Shared)
                .subscribe();

        Consumer consumer21 = client.newConsumer()
                .topic("my-topic")
                .subscriptionName("my-subscription")
                .subscriptionType(SubscriptionType.Shared)
                .subscribe();
        //  Both consumer1 and consumer 2 is active consumers.
        //  在共享订阅模式中，多个消费者可以附加到相同的订阅，并且消息在消费者之间以轮询分发的方式传递。
        //  Shared 订阅有更好的灵活性，但不能提供顺序保证。

        // Key_shared（Pulsar2.4.0版本起支持）
        Consumer consumer12 = client.newConsumer()
                .topic("my-topic")
                .subscriptionName("my-subscription")
                .subscriptionType(SubscriptionType.Key_Shared)
                .subscribe();

        Consumer consumer22 = client.newConsumer()
                .topic("my-topic")
                .subscriptionName("my-subscription")
                .subscriptionType(SubscriptionType.Key_Shared)
                .subscribe();
        //  Both consumer1 and consumer2 are active consumers.
        //  具有相同key的消息按顺序仅传递给一个消费者。
        //  事先不知道哪些key会被分配给哪个消费者，但一个key只会在同一时间被分配给一个消费者。
        //  如果未指定消息的key，则默认情况下将不带key的消息按顺序分发给一个消费者。

        //  Key_shared与批处理共用的注意点
        //  如果在生产者端启用批处理，默认情况下，具有不同key的消息将被添加到批处理中。broker将把batch分发给消费者，因此默认的批处理机制可能会破坏Key_Shared订阅保证的消息分发语义。生产者需要使用 KeyBasedBatcher 。
        Producer producer12 = client.newProducer()
                .topic("my-topic")
                .batcherBuilder(BatcherBuilder.KEY_BASED)
                .create();
        //  或者生产者可以禁用批处理。
        Producer producer22 = client.newProducer()
                .topic("my-topic")
                .enableBatching(false)
                .create();
    }

}

