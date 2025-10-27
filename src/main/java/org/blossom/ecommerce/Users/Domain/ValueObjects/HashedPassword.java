package org.blossom.ecommerce.Users.Domain.ValueObjects;

import org.blossom.ecommerce.Users.Domain.Enums.ErrorMessage;

import java.util.Objects;

public record HashedPassword(String value) {
    public HashedPassword {
        Objects.requireNonNull(value, ErrorMessage.PASSWORD_NULL.getMessage());

        if (value.isBlank()) {
            throw new IllegalArgumentException(ErrorMessage.PASSWORD_BLANK.getMessage());
        }

        if (!(value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$"))
                || value.length() != 60) {
            throw new IllegalArgumentException(ErrorMessage.PASSWORD_FORMATT.getMessage());
        }
    }

    public static HashedPassword fromRaw(String raw) {
        return new HashedPassword(raw);
    }
}
