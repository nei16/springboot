package com.nei.common.rabbitmq;

import com.nei.config.TopicRabbitConfig;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class TopicRabbitMQListener {

    @SneakyThrows
    @RabbitListener(queues = TopicRabbitConfig.FIRST_TOPIC_QUEUE)
    public void first(Message message, Channel channel, Map<String, Object> map) {
        log.info("Topic First Receive {}", map);

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @SneakyThrows
    @RabbitListener(queues = TopicRabbitConfig.SECOND_TOPIC_QUEUE)
    public void second(Message message, Channel channel, Map<String, Object> map) {
        log.info("Topic Second Receive {}", map);

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

}
