package com.adpro.pembelian.eventdriven;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.*;

class RabbitMQProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private RabbitMQProducer rabbitMQProducer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendMessage() {
        String routingKey = "testRoutingKey";
        String message = "Test message";

        rabbitMQProducer.sendMessage(routingKey, message);

        verify(rabbitTemplate, times(1)).convertAndSend("helloworld", routingKey, message);
    }
}
