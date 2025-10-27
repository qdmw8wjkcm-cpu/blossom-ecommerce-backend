package org.blossom.ecommerce.Products.Domain.Spec;

import org.blossom.ecommerce.Products.Domain.ValueObjects.ProductId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UpdateProductTest {

    @Test
    void testUpdateProduct_ConstructionAndAccessors() {
        // Arrange
        ProductId productId = new ProductId(UUID.randomUUID());
        String title = "Updated Title";
        BigDecimal price = new BigDecimal("123.45");

        // Act
        UpdateProduct spec = new UpdateProduct(productId, title, null, price, 50, "CountryZ", "CityA");

        // Assert
        assertEquals(productId, spec.id());
        assertEquals(title, spec.title());
        assertNull(spec.description());
        assertEquals(price, spec.price());
        assertEquals(50, spec.stock());
        assertEquals("CountryZ", spec.country());
        assertEquals("CityA", spec.city());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        ProductId productId = new ProductId(UUID.randomUUID());
        UpdateProduct spec1 = new UpdateProduct(productId, "Title", null, BigDecimal.ONE, 1, null, null);
        UpdateProduct spec2 = new UpdateProduct(productId, "Title", null, BigDecimal.ONE, 1, null, null);

        // Assert
        assertEquals(spec1, spec2);
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }
}