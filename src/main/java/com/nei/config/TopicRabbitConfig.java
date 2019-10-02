package com.nei.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicRabbitConfig {

    public final static String FIRST_TOPIC_QUEUE = "first_topic_queue";
    public final static String SECOND_TOPIC_QUEUE = "second_topic_queue";
    public static final String TEST_TOPIC_EXCHANGE = "test_topic_exchange";
    public static final String TOPIC_FIRST = "topic.first";
    public static final String TOPIC_SECOND = "topic.second";
    public static final String TOPIC_ALL = "topic.#";

    @Bean
    public Queue firstTopicQueue() {
        return new Queue(TopicRabbitConfig.FIRST_TOPIC_QUEUE, false);
    }

    @Bean
    public Queue secondTopicQueue() {
        return new Queue(TopicRabbitConfig.SECOND_TOPIC_QUEUE, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(TEST_TOPIC_EXCHANGE);
    }

    //将firstQueue和topicExchange绑定,而且绑定的键值为topic.first
    //这样只要是消息携带的路由键是topic.first,才会分发到该队列
    @Bean
    Binding bindingExchangeMessage() {
        return BindingBuilder.bind(firstTopicQueue()).to(exchange()).with(TOPIC_FIRST);
    }

    //将secondQueue和topicExchange绑定,而且绑定的键值为用上通配路由键规则topic.#
    // 这样只要是消息携带的路由键是以topic.开头,都会分发到该队列
    @Bean
    Binding bindingExchangeMessage2() {
        return BindingBuilder.bind(secondTopicQueue()).to(exchange()).with(TOPIC_ALL);
    }

}
