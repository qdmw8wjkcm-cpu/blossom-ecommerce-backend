package org.blossom.ecommerce.Products.Domain.ValueObjects;

import org.blossom.ecommerce.Users.Domain.Enums.ErrorMessage;

import java.util.Objects;
import java.util.UUID;

public record CategoryId(UUID value) {

    public CategoryId {
        Objects.requireNonNull(value, ErrorMessage.CATEGORY_NULL.getMessage());
        if (value.equals(new UUID(0L, 0L))) {
            throw new IllegalArgumentException(ErrorMessage.CATEGORY_ZERO.getMessage());
        }
    }

    public static CategoryId categoryId() {
        return new CategoryId(UUID.randomUUID());
    }

    public static CategoryId fromString(String raw) {
        return new CategoryId(UUID.fromString(raw));
    }
}
