package org.blossom.ecommerce.Orders.Domain.Models;

import org.blossom.ecommerce.Orders.Domain.ValueObjects.ProductId;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


class OrderItemTest {

    private final ProductId defaultProductId = new ProductId(UUID.randomUUID());

    @Test
    void testCreateOrderItem_Success() {
        OrderItem item = new OrderItem(defaultProductId, 5);

        assertNotNull(item);
        assertEquals(defaultProductId, item.getProductId());
        assertEquals(5, item.getQuantity());
    }

    @Test
    void testCreateOrderItem_NullProductId_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> {
            new OrderItem(null, 5);
        });
    }

    @Test
    void testCreateOrderItem_ZeroQuantity_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new OrderItem(defaultProductId, 0);
        });
    }

    @Test
    void testCreateOrderItem_NegativeQuantity_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new OrderItem(defaultProductId, -1);
        });
    }
}
