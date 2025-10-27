package org.blossom.ecommerce.Products.Infrastructure.Rest.Dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NewProductTest {

    @Test
    void testNewProduct_ConstructionAndAccessors() {
        // Arrange
        String title = "Test Product";
        String description = "A description";
        BigDecimal price = new BigDecimal("100.00");
        UUID categoryId = UUID.randomUUID();
        int stock = 10;
        String country = "USA";
        String city = "New York";

        // Act
        NewProduct newProduct = new NewProduct(title, description, price, categoryId, stock, country, city);

        // Assert
        assertEquals(title, newProduct.title());
        assertEquals(description, newProduct.description());
        assertEquals(price, newProduct.price());
        assertEquals(categoryId, newProduct.categoryId());
        assertEquals(stock, newProduct.stock());
        assertEquals(country, newProduct.country());
        assertEquals(city, newProduct.city());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        UUID categoryId = UUID.randomUUID();
        NewProduct product1 = new NewProduct("Title", "Desc", BigDecimal.ONE, categoryId, 1, "C", "C");
        NewProduct product2 = new NewProduct("Title", "Desc", BigDecimal.ONE, categoryId, 1, "C", "C");

        // Assert
        assertEquals(product1, product2);
        assertEquals(product1.hashCode(), product2.hashCode());
    }
}