package org.blossom.ecommerce.Orders.Infrastructure.Rest.Dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AddItemRequestTest {

    @Test
    void testAddItemRequest_ConstructionAndAccessors() {
        // Arrange
        UUID productId = UUID.randomUUID();
        String title = "Test Product";
        BigDecimal unitPrice = new BigDecimal("15.75");
        int quantity = 3;

        // Act
        AddItemRequest request = new AddItemRequest(productId, title, unitPrice, quantity);

        // Assert
        assertEquals(productId, request.productId());
        assertEquals(title, request.title());
        assertEquals(unitPrice, request.unitPrice());
        assertEquals(quantity, request.quantity());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        UUID productId = UUID.randomUUID();
        AddItemRequest request1 = new AddItemRequest(productId, "Prod", BigDecimal.ONE, 1);
        AddItemRequest request2 = new AddItemRequest(productId, "Prod", BigDecimal.ONE, 1);

        // Assert
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }
}