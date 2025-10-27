package org.blossom.ecommerce.Orders.Domain.ValueObjects;

import org.blossom.ecommerce.Users.Domain.Enums.ErrorMessage;

import java.util.Objects;
import java.util.UUID;

public record ProductId(UUID value) {

    public ProductId {
        Objects.requireNonNull(value, ErrorMessage.PRODUCT_NULL.getMessage());
        Objects.requireNonNull(value, ErrorMessage.PRODUCT_BLANK.getMessage());
    }

    public static ProductId productId() {
        return new ProductId(UUID.randomUUID());
    }
    public static ProductId fromString(String raw) {
        return new ProductId(UUID.fromString(raw));
    }
}