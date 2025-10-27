package org.blossom.ecommerce.Orders.Infrastructure.Messaging.Events;

import java.io.Serializable;
import java.util.UUID;

public record PaymentRequestedEvent(UUID orderId) implements Serializable {}