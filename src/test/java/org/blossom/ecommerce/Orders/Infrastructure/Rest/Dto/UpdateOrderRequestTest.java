package org.blossom.ecommerce.Orders.Infrastructure.Rest.Dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateOrderRequestTest {

    @Test
    void testUpdateOrderRequest_ConstructionAndAccessors() {
        // Arrange
        String paymentMethod = "PAYPAL";

        // Act
        UpdateOrderRequest request = new UpdateOrderRequest(paymentMethod);

        // Assert
        assertEquals(paymentMethod, request.paymentMethod());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        UpdateOrderRequest request1 = new UpdateOrderRequest("CREDIT_CARD");
        UpdateOrderRequest request2 = new UpdateOrderRequest("CREDIT_CARD");

        // Assert
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }
}