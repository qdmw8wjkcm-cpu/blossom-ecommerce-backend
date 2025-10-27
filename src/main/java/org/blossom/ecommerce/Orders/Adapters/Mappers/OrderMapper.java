package org.blossom.ecommerce.Orders.Adapters.Mappers;

import org.blossom.ecommerce.Orders.Adapters.Jpa.OrderEntity;
import org.blossom.ecommerce.Orders.Adapters.Jpa.OrderItemEntity;
import org.blossom.ecommerce.Orders.Domain.Enums.OrderStatus;
import org.blossom.ecommerce.Orders.Domain.Enums.PaymentMethod;
import org.blossom.ecommerce.Orders.Domain.Models.Order;
import org.blossom.ecommerce.Orders.Domain.Models.OrderItem;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.OrderId;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.ProductId;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.UserId;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Collectors;

public final class OrderMapper {
    private OrderMapper() {}

    public static OrderEntity toEntity(Order o) {
        OrderEntity e = new OrderEntity();
        e.setId(o.getId() != null ? o.getId().value() : null);
        e.setUserId(o.getUserId().value());
        e.setTotal(o.getTotal());
        e.setDiscount(o.getDiscount());
        e.setCreatedAt(o.getCreatedAt());
        e.setUpdatedAt(o.getUpdatedAt());
        e.setStatus(o.getStatus());
        e.setPaymentMethod(o.getPaymentMethod());

        var items = o.getItems().stream().map(it -> {
            OrderItemEntity ie = new OrderItemEntity();
            ie.setId(null);
            ie.setProductId(it.getProductId().value());
            ie.setQuantity(it.getQuantity());
            return ie;
        }).toList();
        e.setItems(items);
        return e;
    }

    public static Order toDomain(OrderEntity e) {
        var items = e.getItems().stream()
                .map(ie -> new OrderItem(new ProductId(ie.getProductId()), ie.getQuantity()))
                .toList();

        return Order.rehydrate(
                new OrderId(e.getId()),
                new UserId(e.getUserId()),
                items,
                e.getTotal(),
                e.getDiscount(),
                e.getCreatedAt(),
                e.getUpdatedAt(),
                OrderStatus.valueOf(e.getStatus().name()),
                PaymentMethod.valueOf(e.getPaymentMethod().name())
        );
    }
}