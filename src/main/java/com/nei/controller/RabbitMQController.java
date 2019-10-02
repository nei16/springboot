package com.nei.controller;

import com.nei.common.util.CommonUtil;
import com.nei.common.util.MapUtil;
import com.nei.config.DirectRabbitConfig;
import com.nei.config.FanoutRabbitConfig;
import com.nei.config.TopicRabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("rabbitmq")
public class RabbitMQController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/sendDirectMessage")
    public String sendDirectMessage() {
        Map<String, Object> map = MapUtil.newHashMap("messageId", CommonUtil.uuid(), "messageData", "direct", "createTime", CommonUtil.now());

        rabbitTemplate.convertAndSend(DirectRabbitConfig.TEST_DIRECT_EXCHANGE, DirectRabbitConfig.TEST_ROUTING_KEY, map);
        return "ok";
    }

    @GetMapping("/sendTopicMessage1")
    public String sendTopicMessage1() {
        Map<String, Object> map = MapUtil.newHashMap("messageId", CommonUtil.uuid(), "messageData", "first topic", "createTime", CommonUtil.now());

        rabbitTemplate.convertAndSend(TopicRabbitConfig.TEST_TOPIC_EXCHANGE, TopicRabbitConfig.TOPIC_FIRST, map);
        return "ok";
    }

    @GetMapping("/sendTopicMessage2")
    public String sendTopicMessage2() {
        Map<String, Object> map = MapUtil.newHashMap("messageId", CommonUtil.uuid(), "messageData", "second topic", "createTime", CommonUtil.now());

        rabbitTemplate.convertAndSend(TopicRabbitConfig.TEST_TOPIC_EXCHANGE, TopicRabbitConfig.TOPIC_SECOND, map);
        return "ok";
    }

    @GetMapping("/sendFanoutMessage")
    public String sendFanoutMessage() {
        Map<String, Object> map = MapUtil.newHashMap("messageId", CommonUtil.uuid(), "messageData", "fanout", "createTime", CommonUtil.now());

        rabbitTemplate.convertAndSend(FanoutRabbitConfig.TEST_FANOUT_EXCHANGE, null, map);
        return "ok";
    }

}
