package org.blossom.ecommerce.Products.Domain.Spec;

import org.blossom.ecommerce.Products.Domain.ValueObjects.CategoryId;

import java.math.BigDecimal;

public record CreateProductSpec(
        String title,
        String description,
        BigDecimal price,
        CategoryId category,
        int stock,
        String country,
        String city
) {
}