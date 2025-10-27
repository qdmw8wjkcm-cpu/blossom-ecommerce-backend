package org.blossom.ecommerce.Products.Infrastructure.Rest.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.time.Instant;

@Schema(name = "ActivateDiscountRequest", description = "Parámetros para activar un descuento")
public record ActivateDiscountRequest(
        @Schema(description = "Porcentaje de descuento (solo uno de los dos campos debe enviarse)", example = "15")
        @Min(0) @Max(100) Long percentage,

        @Schema(description = "Monto fijo de descuento (solo uno de los dos campos debe enviarse)", example = "100.00")
        @DecimalMin("0.0") BigDecimal amount,

        @Schema(description = "Fecha de inicio del descuento (opcional)", example = "2025-10-30T00:00:00Z")
        Instant since,

        @Schema(description = "Fecha de finalización del descuento (opcional)", example = "2025-11-15T00:00:00Z")
        Instant until
) {}