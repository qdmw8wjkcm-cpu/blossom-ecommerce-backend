package org.blossom.ecommerce.Orders.Infrastructure.Messaging.Events;

import java.io.Serializable;
import java.util.UUID;

public record PaymentProcessedEvent(UUID orderId, boolean approved, String transactionId) implements Serializable {}