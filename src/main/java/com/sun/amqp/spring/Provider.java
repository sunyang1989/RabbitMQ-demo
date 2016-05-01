package com.sun.amqp.spring;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Administrator
 * @date 2016/5/1
 */
@Configuration
public class Provider {
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

    // 创建rabbitTemplate 消息模板类
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(quene);
        return template;
    }

    //创建一个调度
    @Bean
    public ScheduledProducer scheduledProducer() {
        return new ScheduledProducer();
    }

    @Bean
    public BeanPostProcessor postProcessor() {
        return new ScheduledAnnotationBeanPostProcessor();
    }


    static class ScheduledProducer {

        @Autowired
        private volatile RabbitTemplate rabbitTemplate;

        //自增整数
        private final AtomicInteger counter = new AtomicInteger();
        /**
         * 每3秒发送一条消息
         *
         * Spring3中加强了注解的使用，其中计划任务也得到了增强，现在创建一个计划任务只需要两步就完成了：
         创建一个Java类，添加一个无参无返回值的方法，在方法上用@Scheduled注解修饰一下；
         在Spring配置文件中添加三个<task:**** />节点；
         参考：http://zywang.iteye.com/blog/949123
         */
        @Scheduled(fixedRate = 3000)
        public void sendMessage() {
            rabbitTemplate.convertAndSend("Hello World " + counter.incrementAndGet());
        }
    }

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(Provider.class);
    }

}
