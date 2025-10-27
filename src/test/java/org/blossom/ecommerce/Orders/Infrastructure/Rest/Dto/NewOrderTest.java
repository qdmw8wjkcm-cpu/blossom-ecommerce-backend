package org.blossom.ecommerce.Orders.Infrastructure.Rest.Dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NewOrderTest {

    @Test
    void testNewOrder_ConstructionAndAccessors() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String paymentMethod = "CREDIT_CARD";
        UUID productId = UUID.randomUUID();
        NewOrder.Item item = new NewOrder.Item(productId, "Product A", new BigDecimal("10.00"), 2);
        List<NewOrder.Item> items = List.of(item);

        // Act
        NewOrder newOrder = new NewOrder(userId, paymentMethod, items);

        // Assert
        assertEquals(userId, newOrder.userId());
        assertEquals(paymentMethod, newOrder.paymentMethod());
        assertEquals(items, newOrder.items());
        assertEquals(1, newOrder.items().size());
        assertEquals(productId, newOrder.items().get(0).productId());
        assertEquals("Product A", newOrder.items().get(0).title());
        assertEquals(new BigDecimal("10.00"), newOrder.items().get(0).unitPrice());
        assertEquals(2, newOrder.items().get(0).quantity());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        NewOrder.Item item = new NewOrder.Item(productId, "Product A", new BigDecimal("10.00"), 2);
        List<NewOrder.Item> items = List.of(item);

        NewOrder newOrder1 = new NewOrder(userId, "CREDIT_CARD", items);
        NewOrder newOrder2 = new NewOrder(userId, "CREDIT_CARD", items);

        // Assert
        assertEquals(newOrder1, newOrder2);
        assertEquals(newOrder1.hashCode(), newOrder2.hashCode());
    }
}