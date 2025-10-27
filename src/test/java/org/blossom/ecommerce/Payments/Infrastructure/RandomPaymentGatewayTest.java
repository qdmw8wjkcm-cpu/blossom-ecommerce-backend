package org.blossom.ecommerce.Payments.Infrastructure;

import org.blossom.ecommerce.Payments.Domain.PaymentGatewayResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RandomPaymentGatewayTest {

    @Mock
    private Random mockRandom;

    @InjectMocks
    private RandomPaymentGateway randomPaymentGateway;

    @Test
    void testCharge_Approved() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        when(mockRandom.nextBoolean()).thenReturn(true); // Force approval

        // Act
        PaymentGatewayResponse response = randomPaymentGateway.charge(orderId);

        // Assert
        assertNotNull(response);
        assertTrue(response.approved());
        assertNotNull(response.transactionId());
    }

    @Test
    void testCharge_Rejected() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        when(mockRandom.nextBoolean()).thenReturn(false); // Force rejection

        // Act
        PaymentGatewayResponse response = randomPaymentGateway.charge(orderId);

        // Assert
        assertNotNull(response);
        assertFalse(response.approved());
        assertNotNull(response.transactionId());
    }
}