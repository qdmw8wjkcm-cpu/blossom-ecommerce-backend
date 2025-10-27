package org.blossom.ecommerce.Orders.Domain.spec;

import org.blossom.ecommerce.Orders.Domain.ValueObjects.OrderId;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.ProductId;

public record RemoveOrderItem(
        OrderId orderId,
        ProductId productId,
        Integer quantity
) {}