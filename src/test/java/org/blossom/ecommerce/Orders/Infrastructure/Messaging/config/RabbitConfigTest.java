package org.blossom.ecommerce.Orders.Infrastructure.Messaging.config;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.blossom.ecommerce.Orders.Infrastructure.Messaging.config.RabbitConfig.*;
import static org.junit.jupiter.api.Assertions.*;


class RabbitConfigTest {

    @Autowired
    private DirectExchange paymentsExchange;

    @Autowired
    private Queue paymentRequestedQueue;

    @Autowired
    private Queue paymentProcessedQueue;

    @Autowired
    private Binding bindingRequested;

    @Autowired
    private Binding bindingProcessed;

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory;

    @Test
    void contextLoads() {
        assertNotNull(paymentsExchange);
        assertNotNull(paymentRequestedQueue);
        assertNotNull(paymentProcessedQueue);
        assertNotNull(bindingRequested);
        assertNotNull(bindingProcessed);
        assertNotNull(messageConverter);
        assertNotNull(rabbitTemplate);
        assertNotNull(rabbitListenerContainerFactory);
    }

    @Test
    void paymentsExchange_shouldBeConfiguredCorrectly() {
        assertEquals(EXCHANGE, paymentsExchange.getName());
        assertTrue(paymentsExchange.isDurable());
        assertFalse(paymentsExchange.isAutoDelete());
    }

    @Test
    void paymentRequestedQueue_shouldBeConfiguredCorrectly() {
        assertEquals(Q_REQUESTED, paymentRequestedQueue.getName());
        assertTrue(paymentRequestedQueue.isDurable());
    }

    @Test
    void paymentProcessedQueue_shouldBeConfiguredCorrectly() {
        assertEquals(Q_PROCESSED, paymentProcessedQueue.getName());
        assertTrue(paymentProcessedQueue.isDurable());
    }

    @Test
    void bindingRequested_shouldBeConfiguredCorrectly() {
        assertEquals(Q_REQUESTED, bindingRequested.getDestination());
        assertEquals(Binding.DestinationType.QUEUE, bindingRequested.getDestinationType());
        assertEquals(EXCHANGE, bindingRequested.getExchange());
        assertEquals(RK_REQUESTED, bindingRequested.getRoutingKey());
    }

    @Test
    void bindingProcessed_shouldBeConfiguredCorrectly() {
        assertEquals(Q_PROCESSED, bindingProcessed.getDestination());
        assertEquals(Binding.DestinationType.QUEUE, bindingProcessed.getDestinationType());
        assertEquals(EXCHANGE, bindingProcessed.getExchange());
        assertEquals(RK_PROCESSED, bindingProcessed.getRoutingKey());
    }

    @Test
    void rabbitTemplate_shouldHaveMessageConverterAndExchange() {
        assertNotNull(rabbitTemplate.getMessageConverter());
        assertEquals(messageConverter, rabbitTemplate.getMessageConverter());
        assertEquals(EXCHANGE, rabbitTemplate.getExchange());
    }

}