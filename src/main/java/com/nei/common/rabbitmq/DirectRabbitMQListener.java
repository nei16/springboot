package com.nei.common.rabbitmq;

import com.nei.config.DirectRabbitConfig;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class DirectRabbitMQListener {

    @SneakyThrows
    @RabbitListener(queues = DirectRabbitConfig.TEST_DIRECT_QUEUE)
    public void process(Message message, Channel channel, Map<String, Object> map) {
        log.info("Direct Receive {}", map);

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

}
