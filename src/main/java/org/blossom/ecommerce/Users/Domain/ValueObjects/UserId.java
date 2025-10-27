package org.blossom.ecommerce.Users.Domain.ValueObjects;

import org.blossom.ecommerce.Users.Domain.Enums.ErrorMessage;

import java.util.Objects;
import java.util.UUID;

public record UserId(UUID value) {

    public UserId {
        Objects.requireNonNull(value, ErrorMessage.USER_NULL.getMessage());
    }

    public static UserId newId() {
        return new UserId(UUID.randomUUID());
    }

}