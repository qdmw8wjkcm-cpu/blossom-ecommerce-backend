package org.blossom.ecommerce.Users.Domain.ValueObjects;

import org.blossom.ecommerce.Users.Domain.Enums.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @ParameterizedTest
    @ValueSource(strings = {"test@example.com", "user.name@domain.co", "user+alias@gmail.com"})
    void testCreateEmail_ValidEmail_Success(String validEmail) {
        Email email = new Email(validEmail);
        assertNotNull(email);
        assertEquals(validEmail, email.value());
    }

    @Test
    void testCreateEmail_NullValue_ShouldThrowException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            new Email(null);
        });
        assertEquals(ErrorMessage.EMAIL_NULL.getMessage(), exception.getMessage());
    }

    @Test
    void testCreateEmail_BlankValue_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Email("  ");
        });
        assertEquals(ErrorMessage.EMAIL_BLANK.getMessage(), exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"plainaddress", "#@%^%#$@#$@#.com", "@example.com", "email.example.com", "email@example@com"})
    void testCreateEmail_InvalidFormat_ShouldThrowException(String invalidEmail) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Email(invalidEmail);
        });
        assertEquals(ErrorMessage.EMAIL_INVALID.getMessage(), exception.getMessage());
    }
}