package org.blossom.ecommerce.Products.Infrastructure.Rest.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Schema(name = "NewProduct", description = "Datos para crear un nuevo producto")
public record NewProduct(
        @NotBlank
        @Schema(description = "Título del producto", example = "Camiseta deportiva")
        String title,

        @NotBlank
        @Schema(description = "Descripción del producto", example = "Camiseta ligera de poliéster con tecnología DryFit")
        String description,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        @Schema(description = "Precio base", example = "79.99")
        BigDecimal price,

        @NotNull
        @Schema(description = "Identificador de la categoría", example = "2e2a7f51-7f17-4f1b-8b30-9a3af6f7a52a")
        UUID categoryId,

        @Min(0)
        @Schema(description = "Cantidad en stock", example = "50")
        int stock,

        @Schema(description = "País donde se vende el producto", example = "Colombia")
        String country,

        @Schema(description = "Ciudad de disponibilidad", example = "Bogotá")
        String city
) {}