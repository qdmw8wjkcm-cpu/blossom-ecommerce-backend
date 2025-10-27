package org.blossom.ecommerce.Orders.Application.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.blossom.ecommerce.Orders.Infrastructure.Messaging.Events.PaymentRequestedEvent;
import org.blossom.ecommerce.Orders.Infrastructure.Messaging.config.RabbitConfig;
import org.blossom.ecommerce.Payments.Infrastructure.PaymentGateway;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Optional;

import org.blossom.ecommerce.Orders.Domain.Enums.OrderMessage;
import org.blossom.ecommerce.Orders.Domain.Enums.OrderStatus;
import org.blossom.ecommerce.Orders.Domain.Exceptions.DomainValidationException;
import org.blossom.ecommerce.Orders.Domain.Models.Order;
import org.blossom.ecommerce.Orders.Domain.Ports.In.OrderCommandUseCase;
import org.blossom.ecommerce.Orders.Domain.Ports.In.OrderQueryUseCase;
import org.blossom.ecommerce.Orders.Domain.spec.*;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.OrderId;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.UserId;
import org.blossom.ecommerce.Orders.Infrastructure.Ports.Out.OrderRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService implements OrderCommandUseCase, OrderQueryUseCase {

    private final OrderRepository repo;
    private final PaymentGateway paymentGateway;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public Optional<Order> create(CreateOrderSpec spec) {
        if (spec.items() == null || spec.items().isEmpty())
            throw new DomainValidationException(OrderMessage.ITEMS_REQUIRED.getMessage());

        Order order = Order.create(spec.userId(), spec.paymentMethod());
        spec.items().forEach(it -> order.addItem(it.productId(), it.quantity()));
        order.recalculateTotals();
        return Optional.of(repo.save(order));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findById(OrderId id) {
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Order> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Order> findByUser(UserId userId, Pageable pageable) {
        return repo.findByUser(userId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Order> findByStatus(OrderStatus status, Pageable pageable) {
        return repo.findByStatus(status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Order> findByDateRange(Instant start, Instant end, Pageable pageable) {
        return repo.findByDateRange(start, end, pageable);
    }

    @Override
    public Order update(UpdateOrder cmd) {
        var current = repo.findById(cmd.id())
                .orElseThrow(() -> new DomainValidationException(OrderMessage.ORDER_NOT_FOUND.getMessage()));
        if (cmd.paymentMethod() != null && current.getStatus() == OrderStatus.WAITING_PAYMENT) {
            current.changePaymentMethod(cmd.paymentMethod());
        }
        return repo.save(current);
    }

    @Override
    public void delete(OrderId id) {
        var current = repo.findById(id)
                .orElseThrow(() -> new DomainValidationException(OrderMessage.ORDER_NOT_FOUND.getMessage()));
        current.cancel();
        repo.save(current);
    }

    @Override
    public Order addItem(AddOrderItem cmd) {
        var order = repo.findById(cmd.orderId())
                .orElseThrow(() -> new DomainValidationException(OrderMessage.ORDER_NOT_FOUND.getMessage()));
        order.assertMutable();
        order.addItem(cmd.productId(), cmd.quantity());
        order.recalculateTotals();
        return repo.save(order);
    }

    @Override
    public Order removeItem(RemoveOrderItem cmd) {
        var order = repo.findById(cmd.orderId())
                .orElseThrow(() -> new DomainValidationException(OrderMessage.ORDER_NOT_FOUND.getMessage()));
        order.assertMutable();
        order.removeItem(cmd.productId(), cmd.quantity());
        order.recalculateTotals();
        return repo.save(order);
    }

    @Override
    public Order applyDiscount(ApplyDiscount cmd) {
        var order = repo.findById(cmd.orderId())
                .orElseThrow(() -> new DomainValidationException(OrderMessage.ORDER_NOT_FOUND.getMessage()));
        order.applyDiscount(cmd.amount());
        return repo.save(order);
    }

    @Transactional
    public Order pay(PayOrder cmd) {
        var order = repo.findById(cmd.orderId())
                .orElseThrow(() -> new DomainValidationException(OrderMessage.ORDER_NOT_FOUND.getMessage()));

        if (order.getStatus() == OrderStatus.CREATED) {
            order.waitingPayment();
            repo.save(order);
        }

        // Publicar el evento de pago
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.RK_REQUESTED,
                new PaymentRequestedEvent(
                        cmd.orderId().value()
                )
        );
        System.out.println("📤 PaymentRequestedEvent sent for " + order.getId().value());

        return order;
    }

    public Order markAsPaid(OrderId id) {
        var order = repo.findById(id)
                .orElseThrow(() -> new DomainValidationException(OrderMessage.ORDER_NOT_FOUND.getMessage()));
        order.markAsPaid();
        return repo.save(order);
    }

    public Order cancel(OrderId id) {
        var order = repo.findById(id)
                .orElseThrow(() -> new DomainValidationException(OrderMessage.ORDER_NOT_FOUND.getMessage()));
        order.cancel();
        return repo.save(order);
    }
    @Override
    public Order cancel(CancelOrder cmd) {
        var order = repo.findById(cmd.orderId())
                .orElseThrow(() -> new DomainValidationException(OrderMessage.ORDER_NOT_FOUND.getMessage()));
        order.cancel();
        return repo.save(order);
    }

    @Override
    public Order markAsDelivered(OrderId id) {
        var order = repo.findById(id)
                .orElseThrow(() -> new DomainValidationException(OrderMessage.ORDER_NOT_FOUND.getMessage()));
        order.markAsDelivered(Instant.now());
        return repo.save(order);
    }

    @Override
    public Order markAsDelivered(MarkAsDelivered cmd) {
        return markAsDelivered(cmd.orderId());
    }

    @Override
    public Order refund(RefundOrder cmd) {
        var order = repo.findById(cmd.orderId())
                .orElseThrow(() -> new DomainValidationException(OrderMessage.ORDER_NOT_FOUND.getMessage()));
        order.refund(cmd.amount());
        return repo.save(order);
    }

}