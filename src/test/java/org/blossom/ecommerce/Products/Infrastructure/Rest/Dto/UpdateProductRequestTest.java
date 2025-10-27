package org.blossom.ecommerce.Products.Infrastructure.Rest.Dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class UpdateProductRequestTest {

    @Test
    void testUpdateProductRequest_ConstructionAndAccessors() {
        // Arrange
        String title = "Updated Title";
        String description = "Updated Description";
        BigDecimal price = new BigDecimal("150.00");
        Integer stock = 20;
        String country = "Canada";
        String city = "Toronto";

        // Act
        UpdateProductRequest request = new UpdateProductRequest(title, description, price, stock, country, city);

        // Assert
        assertEquals(title, request.title());
        assertEquals(description, request.description());
        assertEquals(price, request.price());
        assertEquals(stock, request.stock());
        assertEquals(country, request.country());
        assertEquals(city, request.city());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        UpdateProductRequest request1 = new UpdateProductRequest("T1", "D1", BigDecimal.TEN, 1, "C1", "C1");
        UpdateProductRequest request2 = new UpdateProductRequest("T1", "D1", BigDecimal.TEN, 1, "C1", "C1");

        // Assert
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }
}