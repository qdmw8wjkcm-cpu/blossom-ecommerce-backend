package org.blossom.ecommerce.Users.Domain.ValueObjects;

import org.blossom.ecommerce.Users.Domain.Enums.ErrorMessage;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderIdTest {

    @Test
    void testCreateOrderId_Success() {
        UUID uuid = UUID.randomUUID();
        OrderId orderId = new OrderId(uuid);
        assertNotNull(orderId);
        assertEquals(uuid, orderId.value());
    }

    @Test
    void testCreateOrderId_NullValue_ShouldThrowException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            new OrderId(null);
        });
        assertEquals(ErrorMessage.ORDER_NULL.getMessage(), exception.getMessage());
    }

    @Test
    void testFrom_ValidString_Success() {
        String uuidString = UUID.randomUUID().toString();
        OrderId orderId = OrderId.from(uuidString);
        assertNotNull(orderId);
        assertEquals(UUID.fromString(uuidString), orderId.value());
    }

    @Test
    void testFrom_NullString_ShouldThrowException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            OrderId.from(null);
        });
        assertEquals(ErrorMessage.ORDER_NULL.getMessage(), exception.getMessage());
    }

    @Test
    void testFrom_InvalidString_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            OrderId.from("invalid-uuid-string");
        });
    }

    @Test
    void testEqualsAndHashCode() {
        UUID uuid = UUID.randomUUID();
        OrderId orderId1 = new OrderId(uuid);
        OrderId orderId2 = new OrderId(uuid);
        assertEquals(orderId1, orderId2);
        assertEquals(orderId1.hashCode(), orderId2.hashCode());
    }
}