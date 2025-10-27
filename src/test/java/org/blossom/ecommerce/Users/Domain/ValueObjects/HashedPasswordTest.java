package org.blossom.ecommerce.Users.Domain.ValueObjects;

import org.blossom.ecommerce.Users.Domain.Enums.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class HashedPasswordTest {

    // Example of a valid BCrypt hash
    private final String validBcryptHash = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";

    @Test
    void testCreateHashedPassword_ValidHash_Success() {
        HashedPassword hashedPassword = new HashedPassword(validBcryptHash);
        assertNotNull(hashedPassword);
        assertEquals(validBcryptHash, hashedPassword.value());
    }

    @Test
    void testCreateHashedPassword_NullValue_ShouldThrowException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            new HashedPassword(null);
        });
        assertEquals(ErrorMessage.PASSWORD_NULL.getMessage(), exception.getMessage());
    }

    @Test
    void testCreateHashedPassword_BlankValue_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new HashedPassword("   ");
        });
        assertEquals(ErrorMessage.PASSWORD_BLANK.getMessage(), exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "short", // Invalid length
            "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhW", // Invalid length (59)
            "$2x$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy", // Invalid prefix
            "just a random string of 60 characters to test the length rule.."})
    void testCreateHashedPassword_InvalidFormat_ShouldThrowException(String invalidHash) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new HashedPassword(invalidHash);
        });
        assertEquals(ErrorMessage.PASSWORD_FORMATT.getMessage(), exception.getMessage());
    }

    @Test
    void testFromRaw_Success() {
        HashedPassword hashedPassword = HashedPassword.fromRaw(validBcryptHash);
        assertNotNull(hashedPassword);
        assertEquals(validBcryptHash, hashedPassword.value());
    }
}