package com.wjjzst.queue.kafka.kafkaspringbootconsumer.consumer;

import com.alibaba.fastjson.JSON;
import com.wjjzst.common.constants.QueueKafkaConstant;
import com.wjjzst.common.dtos.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 类功能描述：<br>
 * <ul>
 * <li>类功能描述1<br>
 * <li>类功能描述2<br>
 * <li>类功能描述3<br>
 * </ul>
 * 修改记录：<br>
 * <ul>
 * <li>修改记录描述1<br>
 * <li>修改记录描述2<br>
 * <li>修改记录描述3<br>
 * </ul>
 *
 * @author xuefl
 * @version 5.0 since 2020-01-13
 */
@Component
@Slf4j
@EnableKafka
public class KafkaConsumer {

      // 相同的group_id下 通过控制partition切换
//    @KafkaListener(topics = QueueKafkaConstant.KAFKA_PRODUCER_TOPIC_TEST, groupId = QueueKafkaConstant.KAFKA_PRODUCER_TOPIC_GROUP1)
//    public void topic_test(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
//        Optional message = Optional.ofNullable(record.value());
//        if (message.isPresent()) {
//            Object msg = message.get();
//            OrderDTO orderDTO = JSON.parseObject(msg.toString(), OrderDTO.class);
//            log.info("group_id: {} 消费了：Topic: {} , partition: {} , Message: {}", QueueKafkaConstant.KAFKA_PRODUCER_TOPIC_GROUP1,
//                    topic, orderDTO.getId() % QueueKafkaConstant.KAFKA_PRODUCER_PARTITION_SIZE, msg);
//            ack.acknowledge();
//        }
//    }

    // 多一个group_id 等于就多订阅一份数据
    @KafkaListener(topics = QueueKafkaConstant.KAFKA_PRODUCER_TOPIC_TEST, groupId = QueueKafkaConstant.KAFKA_PRODUCER_TOPIC_GROUP2)
    public void topic_test1(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional message = Optional.ofNullable(record.value());
        if (message.isPresent()) {
            Object msg = message.get();
            OrderDTO orderDTO = JSON.parseObject(msg.toString(), OrderDTO.class);
            log.info("group_id: {} 消费了：Topic: {} , partition: {} , Message: {}", QueueKafkaConstant.KAFKA_PRODUCER_TOPIC_GROUP2,
                    topic, orderDTO.getId() % QueueKafkaConstant.KAFKA_PRODUCER_PARTITION_SIZE, msg);
            ack.acknowledge();
        }
    }

//    @KafkaListener(topics = QueueKafkaConstant.KAFKA_PRODUCER_TOPIC_TEST, groupId = QueueKafkaConstant.KAFKA_PRODUCER_TOPIC_GROUP1,
//            topicPartitions = @TopicPartition(topic = QueueKafkaConstant.KAFKA_PRODUCER_TOPIC_TEST, partitions = {"0", "1", "2"}))
//    public void topic_test(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
//
//        Optional message = Optional.ofNullable(record.value());
//        if (message.isPresent()) {
//            Object msg = message.get();
//            log.info(QueueKafkaConstant.KAFKA_PRODUCER_TOPIC_GROUP1 + " topic_test 消费了： Topic:" + topic + ",Message:" + msg);
//            ack.acknowledge();
//        }
//    }
}