package org.blossom.ecommerce.Products.Infrastructure.Rest.Dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ActivateDiscountRequestTest {

    @Test
    void testActivateDiscountRequest_ConstructionAndAccessors() {
        // Arrange
        Long percentage = 10L;
        BigDecimal amount = new BigDecimal("5.00");
        Instant since = Instant.now();
        Instant until = since.plusSeconds(3600);

        // Act
        ActivateDiscountRequest request = new ActivateDiscountRequest(percentage, amount, since, until);

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
        ActivateDiscountRequest request1 = new ActivateDiscountRequest(15L, null, now, now.plusSeconds(100));
        ActivateDiscountRequest request2 = new ActivateDiscountRequest(15L, null, now, now.plusSeconds(100));

        // Assert
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }
}