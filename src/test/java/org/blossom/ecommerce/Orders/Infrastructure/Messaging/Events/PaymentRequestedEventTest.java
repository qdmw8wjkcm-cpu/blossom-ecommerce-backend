package org.blossom.ecommerce.Orders.Infrastructure.Messaging.Events;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRequestedEventTest {

    @Test
    void testPaymentRequestedEvent_ConstructionAndAccessors() {
        // Arrange
        UUID orderId = UUID.randomUUID();

        // Act
        PaymentRequestedEvent event = new PaymentRequestedEvent(orderId);

        // Assert
        assertEquals(orderId, event.orderId());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        PaymentRequestedEvent event1 = new PaymentRequestedEvent(orderId);
        PaymentRequestedEvent event2 = new PaymentRequestedEvent(orderId);
        PaymentRequestedEvent event3 = new PaymentRequestedEvent(UUID.randomUUID());

        // Assert
        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());
        assertNotEquals(event1, event3);
    }
}