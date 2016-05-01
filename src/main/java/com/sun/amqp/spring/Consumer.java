package com.sun.amqp.spring;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 * @date 2016/5/1
 */
@Configuration
public class Consumer {
    // 指定队列名称 routingkey的名称默认为Queue的名称，使用Exchange类型为DirectExchange
    protected final static String quene = "hello";

    // 创建链接
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("127.0.0.1");
        connectionFactory.setUsername("sunyang");
        connectionFactory.setPassword("123456");
        connectionFactory.setPort(AMQP.PROTOCOL.PORT);
        return connectionFactory;
    }

    // 创建rabbitAdmin 代理类
    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    // 创建rabbitTemplate 消息模板类
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        // The routing key is set to the name of the queue by the broker for the
        // default exchange.
        template.setRoutingKey(this.quene);
        // Where we will synchronously receive messages from
        template.setQueue(this.quene);
        return template;
    }

    @Bean
    public SimpleMessageListenerContainer listenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setQueueNames(this.quene);
        container.setMessageListener(new MessageListenerAdapter(
                new ReceiveMsgHandler()));
        return container;
    }

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(Consumer.class);
    }
}

class ReceiveMsgHandler {
    public void handleMessage(String text) {
        System.out.println("Received: " + text);
    }
}
