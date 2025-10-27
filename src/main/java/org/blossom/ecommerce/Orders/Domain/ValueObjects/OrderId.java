package org.blossom.ecommerce.Orders.Domain.ValueObjects;

import org.blossom.ecommerce.Orders.Domain.Enums.OrderMessage;
import org.blossom.ecommerce.Users.Domain.Enums.ErrorMessage;

import java.util.Objects;
import java.util.UUID;

public record OrderId(UUID value) {

    public OrderId {
        Objects.requireNonNull(value, OrderMessage.ORDER_NULL.getMessage());
    }

    public static OrderId newId() {
        return new OrderId(UUID.randomUUID());
    }

}