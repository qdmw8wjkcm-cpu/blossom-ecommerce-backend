package org.blossom.ecommerce.Orders.Domain.spec;

import org.blossom.ecommerce.Orders.Domain.ValueObjects.OrderId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RefundOrderTest {

    @Test
    void testRefundOrder_ConstructionAndAccessors() {
        // Arrange
        OrderId orderId = new OrderId(UUID.randomUUID());
        BigDecimal amount = new BigDecimal("99.99");

        // Act
        RefundOrder spec = new RefundOrder(orderId, amount);

        // Assert
        assertEquals(orderId, spec.orderId());
        assertEquals(amount, spec.amount());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        OrderId orderId = new OrderId(UUID.randomUUID());
        BigDecimal amount = new BigDecimal("99.99");

        RefundOrder spec1 = new RefundOrder(orderId, amount);
        RefundOrder spec2 = new RefundOrder(orderId, amount);

        // Assert
        assertEquals(spec1, spec2);
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }
}