package org.blossom.ecommerce.Orders.Infrastructure.Rest.Dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(name = "AddItemRequest")
public record AddItemRequest(UUID productId, String title, BigDecimal unitPrice, int quantity) {}
