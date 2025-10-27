package org.blossom.ecommerce.Orders.Domain.spec;

import org.blossom.ecommerce.Orders.Domain.ValueObjects.OrderId;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.ProductId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AddOrderItemTest {

    @Test
    void testAddOrderItem_ConstructionAndAccessors() {
        // Arrange
        OrderId orderId = new OrderId(UUID.randomUUID());
        ProductId productId = new ProductId(UUID.randomUUID());
        int quantity = 3;

        // Act
        AddOrderItem spec = new AddOrderItem(orderId, productId, quantity);

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
        int quantity = 3;

        AddOrderItem spec1 = new AddOrderItem(orderId, productId, quantity);
        AddOrderItem spec2 = new AddOrderItem(orderId, productId, quantity);

        // Assert
        assertEquals(spec1, spec2);
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }
}