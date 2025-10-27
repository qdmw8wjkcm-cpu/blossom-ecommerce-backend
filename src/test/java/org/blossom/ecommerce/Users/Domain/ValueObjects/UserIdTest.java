package org.blossom.ecommerce.Users.Domain.ValueObjects;

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
    void testEqualsAndHashCode() {
        UUID uuid = UUID.randomUUID();
        UserId userId1 = new UserId(uuid);
        UserId userId2 = new UserId(uuid);
        assertEquals(userId1, userId2);
        assertEquals(userId1.hashCode(), userId2.hashCode());
    }
}