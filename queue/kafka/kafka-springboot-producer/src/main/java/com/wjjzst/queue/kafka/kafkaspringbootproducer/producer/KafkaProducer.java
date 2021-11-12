package com.wjjzst.queue.kafka.kafkaspringbootproducer.producer;

import com.alibaba.fastjson.JSONObject;
import com.wjjzst.common.constants.QueueKafkaConstant;
import com.wjjzst.common.dtos.OrderDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

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
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(OrderDTO obj) {
        String obj2String = JSONObject.toJSONString(obj);
        log.info("准备发送消息为：{}", obj2String);
        //发送消息
        ListenableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(QueueKafkaConstant.KAFKA_PRODUCER_TOPIC_TEST,
                        obj.getId() % QueueKafkaConstant.KAFKA_PRODUCER_PARTITION_SIZE, "key", obj2String);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                //发送失败的处理
                log.info(QueueKafkaConstant.KAFKA_PRODUCER_TOPIC_TEST + " - 生产者 发送消息失败：" + throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                //成功的处理
                log.info(QueueKafkaConstant.KAFKA_PRODUCER_TOPIC_TEST + " - 生产者 发送消息成功：" + stringObjectSendResult.toString());
            }
        });


    }
}