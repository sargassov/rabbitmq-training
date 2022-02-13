package com.rabbitmq.trainig.source.publicists;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.util.Scanner;


public class Publicist {
    private static final String EXCHANGE_NAME = "exchanger";
    private static Scanner scanner;

    public static void main(String[] argv) throws Exception {
        scanner = new Scanner(System.in);
        String routingKey = "";
        while(true){
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {
                channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

                String message = scanner.nextLine();
                String[] messageArr = message.split(" ", 2);

                if(messageArr.length > 1){
                    routingKey = messageArr[0];
                    message = messageArr[1];
                }
                else routingKey = "";


                channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
            }
        }

    }
}
