package org.blossom.ecommerce.Orders.Infrastructure.Rest.Dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class RefundOrderRequestTest {

    @Test
    void testRefundOrderRequest_ConstructionAndAccessors() {
        // Arrange
        BigDecimal amount = new BigDecimal("50.00");

        // Act
        RefundOrderRequest request = new RefundOrderRequest(amount);

        // Assert
        assertEquals(amount, request.amount());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        RefundOrderRequest request1 = new RefundOrderRequest(BigDecimal.valueOf(25.00));
        RefundOrderRequest request2 = new RefundOrderRequest(BigDecimal.valueOf(25.00));

        // Assert
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }
}