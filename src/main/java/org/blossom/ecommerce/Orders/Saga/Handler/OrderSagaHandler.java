package org.blossom.ecommerce.Orders.Saga.Handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.blossom.ecommerce.Orders.Domain.spec.CancelOrder;
import org.blossom.ecommerce.Orders.Infrastructure.Messaging.config.RabbitConfig;
import org.blossom.ecommerce.Orders.Infrastructure.Messaging.Events.PaymentProcessedEvent;
import org.blossom.ecommerce.Orders.Application.Service.OrderService;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.OrderId;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderSagaHandler {

    private final OrderService orderService;

    @RabbitListener(queues = RabbitConfig.Q_PROCESSED, containerFactory = "rabbitListenerContainerFactory")
    public void handlePaymentProcessed(PaymentProcessedEvent event) {
        try {
            if (event.approved()) {
                orderService.markAsPaid(new OrderId(event.orderId()));
                log.info("✅ Order {} marked as PAID | tx={}", event.orderId(), event.transactionId());
            } else {
                orderService.cancel(new CancelOrder(new OrderId(event.orderId())));
                log.info("❌ Order {} canceled | tx={}", event.orderId(), event.transactionId());
            }
        } catch (Exception e) {
            log.error("⚠️ Error processing PaymentProcessedEvent for order {}: {}", event.orderId(), e.getMessage(), e);
        }
    }
}