package org.blossom.ecommerce.Orders.Domain.spec;

import org.blossom.ecommerce.Orders.Domain.ValueObjects.OrderId;

import java.math.BigDecimal;
import java.time.Instant;

public record ApplyDiscount(
        OrderId orderId,
        Long percentage,
        BigDecimal amount,
        Instant since,
        Instant until
) {}