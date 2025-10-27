package org.blossom.ecommerce.Orders.Domain.Ports.In;

import org.blossom.ecommerce.Orders.Domain.Enums.OrderStatus;
import org.blossom.ecommerce.Orders.Domain.Models.Order;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.OrderId;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.UserId;
import org.blossom.ecommerce.Orders.Domain.spec.UpdateOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Optional;

public interface OrderQueryUseCase {
    Optional<Order> findById(OrderId id);
    Page<Order> findAll(Pageable pageable);
    Page<Order> findByUser(UserId userId, Pageable pageable);
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
    Page<Order> findByDateRange(Instant start, Instant end, Pageable pageable);

    Order update(UpdateOrder updateOrder);
}