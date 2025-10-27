package org.blossom.ecommerce.Orders.Domain.ValueObjects;

import org.blossom.ecommerce.Users.Domain.Enums.ErrorMessage;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserIdTest {

    @Test
    void testCreateUserId_Success() {
        UUID uuid = UUID.randomUUID();
        UserId userId = new UserId(uuid);
        assertNotNull(userId);
        assertEquals(uuid, userId.value());
    }

    @Test
    void testCreateUserId_NullValue_ShouldThrowException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            new UserId(null);
        });
        assertEquals(ErrorMessage.USER_NULL.getMessage(), exception.getMessage());
    }

    @Test
    void testNewId_ShouldReturnNonNullUserId() {
        UserId userId = UserId.newId();
        assertNotNull(userId);
        assertNotNull(userId.value());
    }

    @Test
    void testNewId_ShouldReturnDifferentIds() {
        UserId userId1 = UserId.newId();
        UserId userId2 = UserId.newId();
        assertNotEquals(userId1.value(), userId2.value());
    }
}