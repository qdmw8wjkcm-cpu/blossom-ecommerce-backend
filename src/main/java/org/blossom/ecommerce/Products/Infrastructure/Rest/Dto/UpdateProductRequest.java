package org.blossom.ecommerce.Products.Infrastructure.Rest.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

@Schema(name = "UpdateProductRequest", description = "Datos para actualizar un producto existente")
public record UpdateProductRequest(
        @Schema(description = "Nuevo título") String title,
        @Schema(description = "Nueva descripción") String description,
        @DecimalMin("0.0") BigDecimal price,
        @Min(0) Integer stock,
        String country,
        String city
) {}