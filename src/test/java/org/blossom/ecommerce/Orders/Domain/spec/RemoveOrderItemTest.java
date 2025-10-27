package org.blossom.ecommerce.Orders.Domain.spec;

import org.blossom.ecommerce.Orders.Domain.ValueObjects.OrderId;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.ProductId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RemoveOrderItemTest {

    @Test
    void testRemoveOrderItem_ConstructionAndAccessors() {
        // Arrange
        OrderId orderId = new OrderId(UUID.randomUUID());
        ProductId productId = new ProductId(UUID.randomUUID());
        Integer quantity = 2;

        // Act
        RemoveOrderItem spec = new RemoveOrderItem(orderId, productId, quantity);

        // Assert
        assertEquals(orderId, spec.orderId());
        assertEquals(productId, spec.productId());
        assertEquals(quantity, spec.quantity());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        OrderId orderId = new OrderId(UUID.randomUUID());
        ProductId productId = new ProductId(UUID.randomUUID());
        Integer quantity = 2;

        RemoveOrderItem spec1 = new RemoveOrderItem(orderId, productId, quantity);
        RemoveOrderItem spec2 = new RemoveOrderItem(orderId, productId, quantity);

        // Assert
        assertEquals(spec1, spec2);
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }
}