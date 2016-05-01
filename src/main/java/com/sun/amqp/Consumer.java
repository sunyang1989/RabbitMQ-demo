package com.sun.amqp;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * @author yang.sun
 * @date 2016/4/29
 */
public class Consumer {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {

      /*建立连接*/
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");//MQ的IP
        factory.setPort(5672);//MQ端口
        factory.setUsername("sunyang");//MQ用户名
        factory.setPassword("123456");//MQ密码
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

      /*声明要连接的队列*/
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

      /*创建消费者对象，用于读取消息*/
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(QUEUE_NAME, true, consumer);

     /* 读取队列，并且阻塞，即在读到消息之前在这里阻塞，直到等到消息，完成消息的阅读后，继续阻塞循环*/
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println(" [x] Received '" + message + "'");
        }
    }
}
