package org.blossom.ecommerce.Products.Domain.Spec;

import org.blossom.ecommerce.Products.Domain.ValueObjects.CategoryId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CreateProductSpecTest {

    @Test
    void testCreateProductSpec_ConstructionAndAccessors() {
        // Arrange
        String title = "New Product";
        String description = "Product Description";
        BigDecimal price = new BigDecimal("25.50");
        CategoryId category = new CategoryId(UUID.randomUUID());
        int stock = 50;
        String country = "CountryX";
        String city = "CityY";

        // Act
        CreateProductSpec spec = new CreateProductSpec(title, description, price, category, stock, country, city);

        // Assert
        assertEquals(title, spec.title());
        assertEquals(description, spec.description());
        assertEquals(price, spec.price());
        assertEquals(category, spec.category());
        assertEquals(stock, spec.stock());
        assertEquals(country, spec.country());
        assertEquals(city, spec.city());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        CategoryId category = new CategoryId(UUID.randomUUID());
        CreateProductSpec spec1 = new CreateProductSpec("Title", "Desc", BigDecimal.TEN, category, 10, "C", "C");
        CreateProductSpec spec2 = new CreateProductSpec("Title", "Desc", BigDecimal.TEN, category, 10, "C", "C");

        // Assert
        assertEquals(spec1, spec2);
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }
}