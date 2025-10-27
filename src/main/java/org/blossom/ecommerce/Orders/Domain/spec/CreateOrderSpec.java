package org.blossom.ecommerce.Orders.Domain.spec;

import org.blossom.ecommerce.Orders.Domain.Enums.PaymentMethod;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.ProductId;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.UserId;

import java.util.List;

public record CreateOrderSpec(
        UserId userId,
        PaymentMethod paymentMethod,
        List<Item> items
) {
    public record Item(
            ProductId productId,
            int quantity
    ) {}
}