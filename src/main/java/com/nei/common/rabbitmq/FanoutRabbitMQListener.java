package com.nei.common.rabbitmq;

import com.nei.config.FanoutRabbitConfig;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class FanoutRabbitMQListener {

    @SneakyThrows
    @RabbitListener(queues = FanoutRabbitConfig.FIRST_FANOUT_QUEUE)
    public void first(Message message, Channel channel, Map<String, Object> map) {
        log.info("First Fanout Receive {}", map);

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @SneakyThrows
    @RabbitListener(queues = FanoutRabbitConfig.SECOND_FANOUT_QUEUE)
    public void second(Message message, Channel channel, Map<String, Object> map) {
        log.info("Second Fanout Receive {}", map);

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @SneakyThrows
    @RabbitListener(queues = FanoutRabbitConfig.THIRD_FANOUT_QUEUE)
    public void third(Message message, Channel channel, Map<String, Object> map) {
        log.info("Third Fanout Receive {}", map);

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

}
