package org.blossom.ecommerce.Orders.Domain.spec;

import org.blossom.ecommerce.Orders.Domain.ValueObjects.OrderId;

import java.math.BigDecimal;

public record RefundOrder(
        OrderId orderId,
        BigDecimal amount
) {}