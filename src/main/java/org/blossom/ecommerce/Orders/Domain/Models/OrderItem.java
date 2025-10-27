package org.blossom.ecommerce.Orders.Domain.Models;

import org.blossom.ecommerce.Orders.Domain.ValueObjects.ProductId;

import java.util.Objects;

public class OrderItem {
    private final ProductId productId;
    private final int quantity;

    public OrderItem(ProductId productId, int quantity) {
        this.productId = Objects.requireNonNull(productId, "productId");
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");
        this.quantity = quantity;
    }

    public ProductId getProductId() { return productId; }
    public int getQuantity() { return quantity; }
}