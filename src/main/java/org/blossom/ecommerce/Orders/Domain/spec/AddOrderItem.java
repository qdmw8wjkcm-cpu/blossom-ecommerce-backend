package org.blossom.ecommerce.Orders.Domain.spec;

import org.blossom.ecommerce.Orders.Domain.ValueObjects.OrderId;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.ProductId;

public record AddOrderItem(
        OrderId orderId,
        ProductId productId,
        int quantity
) {}