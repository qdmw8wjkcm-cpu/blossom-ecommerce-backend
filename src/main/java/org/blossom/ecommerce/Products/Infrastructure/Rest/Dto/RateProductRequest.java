package org.blossom.ecommerce.Products.Infrastructure.Rest.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(name = "RateProductRequest", description = "Datos para calificar un producto")
public record RateProductRequest(
        @NotNull
        @DecimalMin(value = "0.0")
        @DecimalMax(value = "5.0")
        @Schema(description = "Calificación de 0.0 a 5.0", example = "4.5")
        Double rating
) {}