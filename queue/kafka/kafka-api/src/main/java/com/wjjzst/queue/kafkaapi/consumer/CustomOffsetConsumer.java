package com.wjjzst.queue.kafkaapi.consumer;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;


/**
 * @Author: Wjj
 * @Date: 2020/5/25 1:48 上午
 * @desc:
 */
public class CustomOffsetConsumer {

    public static void main(String[] args) {

        Properties props = new Properties();
        props.put("bootstrap.servers", "hadoop1:9092,hadoop2:9092,hadoop3:9092,hadoop4:9092,hadoop5:9092");
        props.put("group.id", "test");//消费者组，只要group.id相同，就属于同一个消费者组
        props.put("enable.auto.commit", "false");//自动提交offset
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("first"), new ConsumerRebalanceListener() {

            //提交当前负责的分区的offset
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {


            }

            //定位新分配的分区的offset
            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                for (TopicPartition partition : partitions) {
                    Long offset = getPartitionOffset(partition);
                    consumer.seek(partition,offset);
                }
            }
        });


        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {

                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
                commitOffset(topicPartition,record.offset()+1);
            }
        }
    }

    private static void commitOffset(TopicPartition topicPartition, long l) {

    }

    private static Long getPartitionOffset(TopicPartition partition) {
        return null;
    }

}

