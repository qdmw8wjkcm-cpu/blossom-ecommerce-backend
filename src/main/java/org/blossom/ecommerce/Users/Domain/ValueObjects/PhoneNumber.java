package org.blossom.ecommerce.Users.Domain.ValueObjects;

import org.blossom.ecommerce.Users.Domain.Enums.ErrorMessage;

import java.util.regex.Pattern;

public record PhoneNumber(String value) {

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^(\\+\\d{1,3})?\\s?\\d{7,15}$");

    public PhoneNumber {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(ErrorMessage.PHONE_BLANK.getMessage());
        }
        if (!PHONE_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(ErrorMessage.PHONE_INVALID.getMessage() + value);
        }
        value = normalize(value);
    }

    private static String normalize(String number) {
        return number.replaceAll("[\\s\\-()]", "");
    }

}
