package com.sun.amqp;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author yang.sun
 * @date 2016/4/29
 */
public class Provider {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {

      /*使用工厂类建立Connection和Channel，并且设置参数*/
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");//MQ的IP
        factory.setPort(5672);//MQ端口
        factory.setUsername("sunyang");//MQ用户名
        factory.setPassword("123456");//MQ密码
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();


        /*创建消息队列，并且发送消息*/
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        for(int i=0;i<5;i++)
        {
            String message = String.valueOf(i);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }


        /*关闭连接*/
        channel.close();
        connection.close();
    }

}
