package org.blossom.ecommerce.Products.Infrastructure.Rest.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "AddCommentRequest", description = "Comentario sobre un producto")
public record AddCommentRequest(
        @NotBlank
        @Schema(description = "Comentario del usuario", example = "Excelente calidad y entrega rápida")
        String comment
) {}