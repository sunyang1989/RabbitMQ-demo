package com.sun.amqp.spring;

/**
 * Created by sun on 2016/5/2.
 */
public class ReceiveMsgHandler {
    public void handleMessage(String text) {
        System.out.println("Received: " + text);
    }
}
