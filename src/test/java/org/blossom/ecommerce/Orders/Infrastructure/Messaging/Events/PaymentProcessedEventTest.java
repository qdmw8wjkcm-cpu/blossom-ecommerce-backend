package org.blossom.ecommerce.Orders.Infrastructure.Messaging.Events;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PaymentProcessedEventTest {

    @Test
    void testPaymentProcessedEvent_ConstructionAndAccessors() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        boolean approved = true;
        String transactionId = "txn_12345";

        // Act
        PaymentProcessedEvent event = new PaymentProcessedEvent(orderId, approved, transactionId);

        // Assert
        assertEquals(orderId, event.orderId());
        assertEquals(approved, event.approved());
        assertEquals(transactionId, event.transactionId());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        PaymentProcessedEvent event1 = new PaymentProcessedEvent(orderId, true, "txn1");
        PaymentProcessedEvent event2 = new PaymentProcessedEvent(orderId, true, "txn1");
        PaymentProcessedEvent event3 = new PaymentProcessedEvent(UUID.randomUUID(), false, "txn2");

        // Assert
        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());
        assertNotEquals(event1, event3);
    }
}