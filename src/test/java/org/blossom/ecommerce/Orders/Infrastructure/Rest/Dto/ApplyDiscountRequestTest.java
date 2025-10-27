package org.blossom.ecommerce.Orders.Infrastructure.Rest.Dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ApplyDiscountRequestTest {

    @Test
    void testApplyDiscountRequest_ConstructionAndAccessors() {
        // Arrange
        Long percentage = 10L;
        BigDecimal amount = new BigDecimal("20.00");
        Instant since = Instant.now();
        Instant until = since.plusSeconds(3600);

        // Act
        ApplyDiscountRequest request = new ApplyDiscountRequest(percentage, amount, since, until);

        // Assert
        assertEquals(percentage, request.percentage());
        assertEquals(amount, request.amount());
        assertEquals(since, request.since());
        assertEquals(until, request.until());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        Instant now = Instant.now();
        ApplyDiscountRequest request1 = new ApplyDiscountRequest(5L, BigDecimal.ONE, now, now.plusSeconds(100));
        ApplyDiscountRequest request2 = new ApplyDiscountRequest(5L, BigDecimal.ONE, now, now.plusSeconds(100));

        // Assert
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }
}