package org.blossom.ecommerce.Users.Domain.ValueObjects;

import org.blossom.ecommerce.Users.Domain.Enums.ErrorMessage;

import java.util.Objects;
import java.util.regex.Pattern;

public record Email(String value) {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            Pattern.CASE_INSENSITIVE
    );

    public Email {
        Objects.requireNonNull(value, ErrorMessage.EMAIL_NULL.getMessage());

        if (value.isBlank()) {
            throw new IllegalArgumentException(ErrorMessage.EMAIL_BLANK.getMessage());
        }

        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(ErrorMessage.EMAIL_INVALID.getMessage());
        }
    }

}