package org.blossom.ecommerce.Orders.Infrastructure.Rest.Dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PayOrderRequestTest {

    @Test
    void testPayOrderRequest_ConstructionAndAccessors() {
        // Arrange
        String paymentMethod = "PAYPAL";
        String paymentReference = "ref_12345";
        BigDecimal amount = new BigDecimal("100.50");

        // Act
        PayOrderRequest request = new PayOrderRequest(paymentMethod, paymentReference, amount);

        // Assert
        assertEquals(paymentMethod, request.paymentMethod());
        assertEquals(paymentReference, request.paymentReference());
        assertEquals(amount, request.amount());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        PayOrderRequest request1 = new PayOrderRequest("VISA", "ref1", BigDecimal.TEN);
        PayOrderRequest request2 = new PayOrderRequest("VISA", "ref1", BigDecimal.TEN);

        // Assert
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }
}