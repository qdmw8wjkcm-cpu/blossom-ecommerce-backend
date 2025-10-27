package org.blossom.ecommerce.Orders.Domain.spec;

import org.blossom.ecommerce.Orders.Domain.ValueObjects.OrderId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CancelOrderTest {

    @Test
    void testCancelOrder_ConstructionAndAccessors() {
        // Arrange
        OrderId orderId = new OrderId(UUID.randomUUID());

        // Act
        CancelOrder spec = new CancelOrder(orderId);

        // Assert
        assertEquals(orderId, spec.orderId());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        OrderId orderId = new OrderId(UUID.randomUUID());

        CancelOrder spec1 = new CancelOrder(orderId);
        CancelOrder spec2 = new CancelOrder(orderId);

        // Assert
        assertEquals(spec1, spec2);
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }
}