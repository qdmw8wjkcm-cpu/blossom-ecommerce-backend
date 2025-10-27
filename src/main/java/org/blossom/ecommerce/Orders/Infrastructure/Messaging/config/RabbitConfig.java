package org.blossom.ecommerce.Orders.Infrastructure.Messaging.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

    public static final String EXCHANGE     = "payments.x";
    public static final String RK_REQUESTED = "payment.requested";
    public static final String RK_PROCESSED = "payment.processed";
    public static final String Q_REQUESTED  = "payment.requested.q";
    public static final String Q_PROCESSED  = "payment.processed.q";

    @Bean
    public DirectExchange paymentsExchange() {
        return new DirectExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue paymentRequestedQueue() {
        return QueueBuilder.durable(Q_REQUESTED).build();
    }

    @Bean
    public Queue paymentProcessedQueue() {
        return QueueBuilder.durable(Q_PROCESSED).build();
    }

    @Bean
    public Binding bindingRequested(DirectExchange paymentsExchange, Queue paymentRequestedQueue) {
        return BindingBuilder.bind(paymentRequestedQueue)
                .to(paymentsExchange)
                .with(RK_REQUESTED);
    }

    @Bean
    public Binding bindingProcessed(DirectExchange paymentsExchange, Queue paymentProcessedQueue) {
        return BindingBuilder.bind(paymentProcessedQueue)
                .to(paymentsExchange)
                .with(RK_PROCESSED);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf, MessageConverter converter) {
        var tpl = new RabbitTemplate(cf);
        tpl.setMessageConverter(converter);
        tpl.setExchange(EXCHANGE);
        return tpl;
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