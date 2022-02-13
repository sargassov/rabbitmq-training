package com.rabbitmq.trainig.source.followers;

import com.rabbitmq.client.*;

import java.util.Scanner;

public class Follower {
    private static final String EXCHANGE_NAME = "exchanger";
    private static String routingKey = "php";
    private static Scanner scanner;
    private static boolean flag = true;

    public static void main(String[] argv) throws Exception {
        scanner = new Scanner(System.in);
        while(flag){
            flag = false;
            System.out.println("ROUTING KEY = " + routingKey);

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

            String queueName = channel.queueDeclare().getQueue();
            System.out.println("QUEUE NAME: " + queueName);
            ;
            channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                System.out.println(" [*] Waiting for messages with routing key (" + routingKey + "):");
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
                System.out.print("Write new ROUTING KEY: ");
                routingKey = scanner.nextLine();
                if(!routingKey.equals("")) flag = true;
                else System.exit(0);
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });
        }

    }
}
