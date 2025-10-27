package org.blossom.ecommerce.Products.Domain.Spec;

import org.blossom.ecommerce.Products.Domain.ValueObjects.ProductId;

import java.math.BigDecimal;

public record UpdateProduct(
        ProductId id,
        String title,
        String description,
        BigDecimal price,
        Integer stock,
        String country,
        String city
) { }