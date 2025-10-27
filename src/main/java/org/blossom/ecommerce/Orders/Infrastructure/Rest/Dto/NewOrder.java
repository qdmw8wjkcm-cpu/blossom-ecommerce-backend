package org.blossom.ecommerce.Orders.Infrastructure.Rest.Dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Schema(name = "NewOrder")
public record NewOrder(
        UUID userId,
        String paymentMethod,
        List<Item> items
) {
    public record Item(UUID productId, String title, BigDecimal unitPrice, int quantity) {}
}