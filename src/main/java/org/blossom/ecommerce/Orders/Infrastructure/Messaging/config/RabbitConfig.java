package org.blossom.ecommerce.Orders.Infrastructure.Messaging.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

    public static final String EXCHANGE = "payments.x";
    public static final String RK_REQUESTED  = "payment.requested";
    public static final String RK_PROCESSED  = "payment.processed";
    public static final String Q_REQUESTED   = "payment.requested.q";
    public static final String Q_PROCESSED   = "payment.processed.q";

    @Bean
    public Exchange paymentsExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE).durable(true).build();
    }

    @Bean public Queue paymentRequestedQueue() { return QueueBuilder.durable(Q_REQUESTED).build(); }
    @Bean public Queue paymentProcessedQueue() { return QueueBuilder.durable(Q_PROCESSED).build(); }

    @Bean
    public Binding bindRequested(Queue paymentRequestedQueue, Exchange paymentsExchange) {
        return BindingBuilder.bind(paymentRequestedQueue).to(paymentsExchange).with(RK_REQUESTED).noargs();
    }

    @Bean
    public Binding bindProcessed(Queue paymentProcessedQueue, Exchange paymentsExchange) {
        return BindingBuilder.bind(paymentProcessedQueue).to(paymentsExchange).with(RK_PROCESSED).noargs();
    }
    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMapper) {
        return new JacksonJsonMessageConverter(String.valueOf(objectMapper));
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf, MessageConverter mc) {
        RabbitTemplate rt = new RabbitTemplate(cf);
        rt.setMessageConverter(mc);
        return rt;
    }


    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        var factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }
}