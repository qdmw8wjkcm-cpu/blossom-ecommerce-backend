package org.blossom.ecommerce.Orders.Domain.spec;

import org.blossom.ecommerce.Orders.Domain.ValueObjects.OrderId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ApplyDiscountTest {

    @Test
    void testApplyDiscount_ConstructionAndAccessors() {
        // Arrange
        OrderId orderId = new OrderId(UUID.randomUUID());
        Long percentage = 10L;
        BigDecimal amount = new BigDecimal("15.50");
        Instant since = Instant.now();
        Instant until = since.plusSeconds(3600);

        // Act
        ApplyDiscount spec = new ApplyDiscount(orderId, percentage, amount, since, until);

        // Assert
        assertEquals(orderId, spec.orderId());
        assertEquals(percentage, spec.percentage());
        assertEquals(amount, spec.amount());
        assertEquals(since, spec.since());
        assertEquals(until, spec.until());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        OrderId orderId = new OrderId(UUID.randomUUID());
        Long percentage = 10L;
        BigDecimal amount = new BigDecimal("15.50");
        Instant since = Instant.now();
        Instant until = since.plusSeconds(3600);

        ApplyDiscount spec1 = new ApplyDiscount(orderId, percentage, amount, since, until);
        ApplyDiscount spec2 = new ApplyDiscount(orderId, percentage, amount, since, until);

        // Assert
        assertEquals(spec1, spec2);
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }
}