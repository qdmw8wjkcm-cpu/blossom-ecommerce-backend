package org.blossom.ecommerce.Orders.Domain.spec;

import org.blossom.ecommerce.Orders.Domain.ValueObjects.OrderId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MarkAsDeliveredTest {

    @Test
    void testMarkAsDelivered_ConstructionAndAccessors() {
        // Arrange
        OrderId orderId = new OrderId(UUID.randomUUID());

        // Act
        MarkAsDelivered spec = new MarkAsDelivered(orderId);

        // Assert
        assertEquals(orderId, spec.orderId());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        OrderId orderId = new OrderId(UUID.randomUUID());

        MarkAsDelivered spec1 = new MarkAsDelivered(orderId);
        MarkAsDelivered spec2 = new MarkAsDelivered(orderId);

        // Assert
        assertEquals(spec1, spec2);
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }
}