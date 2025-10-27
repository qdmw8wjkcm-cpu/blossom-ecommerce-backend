package org.blossom.ecommerce.Products.Domain.Spec;

import org.blossom.ecommerce.Products.Domain.ValueObjects.ProductId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ActivateDiscountTest {

    @Test
    void testActivateDiscount_ConstructionAndAccessors() {
        // Arrange
        ProductId productId = new ProductId(UUID.randomUUID());
        Long percentage = 15L;
        BigDecimal amount = new BigDecimal("10.00");
        Instant since = Instant.now();
        Instant until = since.plusSeconds(3600);

        // Act
        ActivateDiscount spec = new ActivateDiscount(productId, percentage, amount, since, until);

        // Assert
        assertEquals(productId, spec.id());
        assertEquals(percentage, spec.percentage());
        assertEquals(amount, spec.amount());
        assertEquals(since, spec.since());
        assertEquals(until, spec.until());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        ProductId productId = new ProductId(UUID.randomUUID());
        Instant now = Instant.now();
        ActivateDiscount spec1 = new ActivateDiscount(productId, 10L, BigDecimal.ONE, now, now.plusSeconds(100));
        ActivateDiscount spec2 = new ActivateDiscount(productId, 10L, BigDecimal.ONE, now, now.plusSeconds(100));

        // Assert
        assertEquals(spec1, spec2);
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }
}