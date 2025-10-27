package org.blossom.ecommerce.Products.Domain.Spec;

import org.blossom.ecommerce.Products.Domain.ValueObjects.ProductId;

public record RateProduct(
        ProductId id,
        Double rating
) { }