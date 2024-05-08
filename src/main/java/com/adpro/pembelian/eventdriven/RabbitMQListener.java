package com.adpro.pembelian.eventdriven;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {


    @RabbitListener(queues = "order-status-queue")
    public void receiveOrderMessage(String message) {
        System.out.println("Received message for order: " + message);
        // Lakukan sesuatu dengan pesan yang diterima untuk use case order
    }
}
