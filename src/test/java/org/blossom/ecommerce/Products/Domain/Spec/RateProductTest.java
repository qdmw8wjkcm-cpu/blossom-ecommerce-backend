package org.blossom.ecommerce.Products.Domain.Spec;

import org.blossom.ecommerce.Products.Domain.ValueObjects.ProductId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RateProductTest {

    @Test
    void testRateProduct_ConstructionAndAccessors() {
        // Arrange
        ProductId productId = new ProductId(UUID.randomUUID());
        Double rating = 4.5;

        // Act
        RateProduct spec = new RateProduct(productId, rating);

        // Assert
        assertEquals(productId, spec.id());
        assertEquals(rating, spec.rating());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        ProductId productId = new ProductId(UUID.randomUUID());
        RateProduct spec1 = new RateProduct(productId, 3.0);
        RateProduct spec2 = new RateProduct(productId, 3.0);

        // Assert
        assertEquals(spec1, spec2);
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }
}