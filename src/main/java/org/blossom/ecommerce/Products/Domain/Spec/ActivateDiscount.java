package org.blossom.ecommerce.Products.Domain.Spec;

import org.blossom.ecommerce.Products.Domain.ValueObjects.ProductId;

import java.math.BigDecimal;
import java.time.Instant;

public record ActivateDiscount(
        ProductId id,
        Long percentage,
        BigDecimal amount,
        Instant since,
        Instant until
) { }