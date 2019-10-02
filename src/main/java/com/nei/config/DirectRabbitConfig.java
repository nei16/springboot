package com.nei.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectRabbitConfig {

    public static final String TEST_DIRECT_QUEUE = "test_direct_queue";
    public static final String TEST_DIRECT_EXCHANGE = "test_direct_exchange";
    public static final String TEST_ROUTING_KEY = "test_routing_key";

    @Bean
    public Queue testDirectQueue() {
        return new Queue(TEST_DIRECT_QUEUE, false);
    }

    @Bean
    public DirectExchange testDirectExchange() {
        return new DirectExchange(TEST_DIRECT_EXCHANGE);
    }

    @Bean
    public Binding bindingDirect() {
        return BindingBuilder.bind(testDirectQueue()).to(testDirectExchange()).with(TEST_ROUTING_KEY);
    }

}
