package org.blossom.ecommerce.Orders.Infrastructure.Rest.Dto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(name = "PayOrderRequest")
public record PayOrderRequest(
        String paymentMethod,
        String paymentReference,
        BigDecimal amount
) {}