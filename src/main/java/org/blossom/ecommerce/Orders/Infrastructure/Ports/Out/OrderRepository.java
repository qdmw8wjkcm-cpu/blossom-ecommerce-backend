package org.blossom.ecommerce.Orders.Infrastructure.Ports.Out;

import org.blossom.ecommerce.Orders.Domain.Models.Order;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.OrderId;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.UserId;
import org.blossom.ecommerce.Orders.Domain.Enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(OrderId id);
    Page<Order> findAll(Pageable pageable);
    Page<Order> findByUser(UserId userId, Pageable pageable);
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
    Page<Order> findByDateRange(Instant start, Instant end, Pageable pageable);
}