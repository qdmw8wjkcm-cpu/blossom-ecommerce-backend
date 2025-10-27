package org.blossom.ecommerce.Orders.Infrastructure.Rest.Dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RemoveItemRequest")
public record RemoveItemRequest(int quantity) {}