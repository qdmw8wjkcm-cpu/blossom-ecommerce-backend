package org.blossom.ecommerce.Orders.Infrastructure.Rest.Dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RemoveItemRequestTest {

    @Test
    void testRemoveItemRequest_ConstructionAndAccessors() {
        // Arrange
        int quantity = 5;

        // Act
        RemoveItemRequest request = new RemoveItemRequest(quantity);

        // Assert
        assertEquals(quantity, request.quantity());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        RemoveItemRequest request1 = new RemoveItemRequest(10);
        RemoveItemRequest request2 = new RemoveItemRequest(10);

        // Assert
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }
}