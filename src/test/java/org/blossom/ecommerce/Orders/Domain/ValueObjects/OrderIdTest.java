package org.blossom.ecommerce.Orders.Domain.ValueObjects;

import org.blossom.ecommerce.Orders.Domain.Enums.OrderMessage;
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
        assertEquals(OrderMessage.ORDER_NULL.getMessage(), exception.getMessage());
    }

    @Test
    void testNewId_ShouldReturnNonNullOrderId() {
        OrderId orderId = OrderId.newId();
        assertNotNull(orderId);
        assertNotNull(orderId.value());
    }

    @Test
    void testNewId_ShouldReturnDifferentIds() {
        OrderId orderId1 = OrderId.newId();
        OrderId orderId2 = OrderId.newId();
        assertNotEquals(orderId1.value(), orderId2.value());
    }
}