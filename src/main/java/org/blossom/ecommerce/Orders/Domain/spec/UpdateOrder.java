package org.blossom.ecommerce.Orders.Domain.spec;

import org.blossom.ecommerce.Orders.Domain.Enums.PaymentMethod;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.OrderId;

public record UpdateOrder(
        OrderId id,
        PaymentMethod paymentMethod
) {}