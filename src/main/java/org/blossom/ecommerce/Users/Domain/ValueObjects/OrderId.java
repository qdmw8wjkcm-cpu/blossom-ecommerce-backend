package org.blossom.ecommerce.Users.Domain.ValueObjects;

import org.blossom.ecommerce.Users.Domain.Enums.ErrorMessage;

import java.util.Objects;
import java.util.UUID;

public record OrderId(UUID value) {

    public OrderId {
        Objects.requireNonNull(value, ErrorMessage.ORDER_NULL.getMessage());
    }

    public static OrderId from(String id) {
        Objects.requireNonNull(id, ErrorMessage.ORDER_NULL.getMessage());
        return new OrderId(UUID.fromString(id));
    }

}