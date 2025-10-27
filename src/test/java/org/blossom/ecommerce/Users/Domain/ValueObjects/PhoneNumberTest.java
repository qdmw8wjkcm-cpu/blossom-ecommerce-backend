package org.blossom.ecommerce.Users.Domain.ValueObjects;

import org.blossom.ecommerce.Users.Domain.Enums.ErrorMessage;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PhoneNumberTest {

    @ParameterizedTest
    @ValueSource(strings = {"1234567", "+11234567890", "+57 3001234567"})
    void testCreatePhoneNumber_ValidNumber_Success(String validNumber) {
        PhoneNumber phoneNumber = new PhoneNumber(validNumber);
        assertNotNull(phoneNumber);
        // The record's value field will be normalized
        assertEquals(validNumber.replaceAll("[\\s\\-()]", ""), phoneNumber.value());
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "   " })
    void testCreatePhoneNumber_BlankNumber_ShouldThrowException(String blankNumber) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new PhoneNumber(blankNumber);
        });
        assertEquals(ErrorMessage.PHONE_BLANK.getMessage(), exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"123", "+1 123", "abcde", "+12345678901234567"})
    void testCreatePhoneNumber_InvalidFormat_ShouldThrowException(String invalidNumber) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new PhoneNumber(invalidNumber);
        });
        assertTrue(exception.getMessage().startsWith(ErrorMessage.PHONE_INVALID.getMessage()));
    }
}