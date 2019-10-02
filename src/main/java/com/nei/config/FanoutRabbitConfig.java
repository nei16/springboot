package com.nei.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 创建三个队列 ：first_fanout_queue, second_fanout_queue, third_fanout_queue
 * 将三个队列都绑定在交换机 fanoutExchange 上
 * 因为是扇型交换机, 路由键无需配置,配置也不起作用
 */
@Configuration
public class FanoutRabbitConfig {

    public static final String FIRST_FANOUT_QUEUE = "first_fanout_queue";
    public static final String SECOND_FANOUT_QUEUE = "second_fanout_queue";
    public static final String THIRD_FANOUT_QUEUE = "third_fanout_queue";
    public static final String TEST_FANOUT_EXCHANGE = "test_fanout_exchange";

    @Bean
    public Queue firstFanoutQueue() {
        return new Queue(FIRST_FANOUT_QUEUE);
    }

    @Bean
    public Queue secondFanoutQueue() {
        return new Queue(SECOND_FANOUT_QUEUE);
    }

    @Bean
    public Queue thirdFanoutQueue() {
        return new Queue(THIRD_FANOUT_QUEUE);
    }

    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange(TEST_FANOUT_EXCHANGE);
    }

    @Bean
    Binding bindingExchangeA() {
        return BindingBuilder.bind(firstFanoutQueue()).to(fanoutExchange());
    }

    @Bean
    Binding bindingExchangeB() {
        return BindingBuilder.bind(secondFanoutQueue()).to(fanoutExchange());
    }

    @Bean
    Binding bindingExchangeC() {
        return BindingBuilder.bind(thirdFanoutQueue()).to(fanoutExchange());
    }

}
