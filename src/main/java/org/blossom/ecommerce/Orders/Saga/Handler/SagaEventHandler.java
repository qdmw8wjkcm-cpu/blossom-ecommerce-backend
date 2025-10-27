package org.blossom.ecommerce.Orders.Saga.Handler;

import lombok.RequiredArgsConstructor;
import org.blossom.ecommerce.Orders.Infrastructure.Messaging.Events.PaymentProcessedEvent;
import org.blossom.ecommerce.Orders.Infrastructure.Messaging.Events.PaymentRequestedEvent;
import org.blossom.ecommerce.Orders.Infrastructure.Messaging.config.RabbitConfig;
import org.blossom.ecommerce.Payments.Infrastructure.PaymentGateway;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SagaEventHandler {

    private final PaymentGateway gateway;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitConfig.Q_REQUESTED, containerFactory = "rabbitListenerContainerFactory")
    public void handlePaymentRequest(PaymentRequestedEvent event) {
        var resp = gateway.charge(event.orderId());

        var processed = new PaymentProcessedEvent(
                event.orderId(),
                resp.approved(),
                resp.transactionId()
        );

        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.RK_PROCESSED,
                processed
        );

        System.out.println("📨 Sent PaymentProcessedEvent for order " + event.orderId()
                + " approved=" + resp.approved()
                + " txId=" + resp.transactionId());
    }
}