package org.blossom.ecommerce.Products.Infrastructure.Rest.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.blossom.ecommerce.Products.Application.Service.ProductService;
import org.blossom.ecommerce.Products.Domain.Models.Product;
import org.blossom.ecommerce.Products.Domain.Spec.*;
import org.blossom.ecommerce.Products.Domain.ValueObjects.CategoryId;
import org.blossom.ecommerce.Products.Domain.ValueObjects.ProductId;
import org.blossom.ecommerce.Products.Infrastructure.Rest.Dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Endpoints para administración y consulta de productos")
public class ProductController {

    private final ProductService service;

    @Operation(summary = "Crear producto",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Creado",
                            content = @Content(schema = @Schema(implementation = Product.class))),
                    @ApiResponse(responseCode = "400", description = "Validación fallida")
            })
    @PostMapping
    public ResponseEntity<Product> create(@Valid @RequestBody NewProduct dto) {
        CreateProductSpec spec = new CreateProductSpec(
                dto.title(),
                dto.description(),
                dto.price(),
                new CategoryId(dto.categoryId()),
                dto.stock(),
                dto.country(),
                dto.city()
        );
        Product saved = service.create(spec).orElseThrow();
        return ResponseEntity.status(201).body(saved);
    }

    @Operation(summary = "Actualizar producto (parcial)",
            description = "Actualiza campos editables del producto",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Actualizado",
                            content = @Content(schema = @Schema(implementation = Product.class))),
                    @ApiResponse(responseCode = "404", description = "No encontrado")
            })
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateProductRequest dto) {

        UpdateProduct cmd = new UpdateProduct(
                new ProductId(id),
                dto.title(),
                dto.description(),
                dto.price(),
                dto.stock(),
                dto.country(),
                dto.city()
        );
        Product updated = service.update(cmd);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Eliminar lógicamente producto",
            description = "Marca el producto como DELETE (soft-delete)",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Eliminado"),
                    @ApiResponse(responseCode = "404", description = "No encontrado")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        service.delete(new ProductId(id));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Activar descuento",
            description = "Activa descuento por porcentaje o monto fijo (uno u otro). Opcionalmente se puede indicar ventana temporal.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Descuento activado",
                            content = @Content(schema = @Schema(implementation = Product.class))),
                    @ApiResponse(responseCode = "400", description = "Validación fallida"),
                    @ApiResponse(responseCode = "404", description = "No encontrado")
            })
    @PostMapping("/{id}/discount")
    public ResponseEntity<Product> activateDiscount(
            @PathVariable("id") UUID id,
            @Valid @RequestBody ActivateDiscountRequest dto
    ) {
        ActivateDiscount cmd = new ActivateDiscount(
                new ProductId(id),
                dto.percentage(),
                dto.amount(),
                dto.since(),
                dto.until()
        );
        Product updated = service.activateDiscount(cmd);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Desactivar descuento",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Descuento desactivado",
                            content = @Content(schema = @Schema(implementation = Product.class))),
                    @ApiResponse(responseCode = "404", description = "No encontrado")
            })
    @DeleteMapping("/{id}/discount")
    public ResponseEntity<Product> deactivateDiscount(@PathVariable("id") UUID id) {
        Product updated = service.deactivateDiscount(new ProductId(id));
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Calificar producto",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Calificación aplicada",
                            content = @Content(schema = @Schema(implementation = Product.class))),
                    @ApiResponse(responseCode = "400", description = "Validación fallida"),
                    @ApiResponse(responseCode = "404", description = "No encontrado")
            })
    @PostMapping("/{id}/rating")
    public ResponseEntity<Product> rate(
            @PathVariable("id") UUID id,
            @Valid @RequestBody RateProductRequest dto
    ) {
        RateProduct cmd = new RateProduct(new ProductId(id), dto.rating());
        Product updated = service.rate(cmd);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Agregar comentario",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comentario agregado",
                            content = @Content(schema = @Schema(implementation = Product.class))),
                    @ApiResponse(responseCode = "400", description = "Validación fallida"),
                    @ApiResponse(responseCode = "404", description = "No encontrado")
            })
    @PostMapping("/{id}/comments")
    public ResponseEntity<Product> addComment(
            @PathVariable("id") UUID id,
            @Valid @RequestBody AddCommentRequest dto
    ) {
        AddComment cmd = new AddComment(new ProductId(id), dto.comment());
        Product updated = service.addComment(cmd);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Buscar por id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = Product.class))),
                    @ApiResponse(responseCode = "404", description = "No encontrado")
            })
    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable("id") UUID id) {
        Optional<Product> p = service.findById(new ProductId(id));
        return p.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Listado de productos (paginado y con filtros simples)",
            description = """
                    Filtros soportados (se aplica el primero que venga, en este orden):
                    categoryId, (minPrice,maxPrice), title, minRating, discounted=true, country, city.
                    Si no se envía ningún filtro, retorna todo.
                    """
    )
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping
    public ResponseEntity<Page<Product>> list(
            @Parameter(description = "Id de categoría para filtrar")
            @RequestParam(required = false) UUID categoryId,

            @Parameter(description = "Precio mínimo")
            @RequestParam(required = false) BigDecimal minPrice,

            @Parameter(description = "Precio máximo")
            @RequestParam(required = false) BigDecimal maxPrice,

            @Parameter(description = "El título contiene...")
            @RequestParam(required = false) String title,

            @Parameter(description = "Calificación mínima (0..5)")
            @RequestParam(required = false) Double minRating,

            @Parameter(description = "Solo con descuento")
            @RequestParam(required = false, defaultValue = "false") boolean discounted,

            @Parameter(description = "País")
            @RequestParam(required = false) String country,

            @Parameter(description = "Ciudad")
            @RequestParam(required = false) String city,

            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<Product> page;
        if (categoryId != null) {
            page = service.findByCategory(new CategoryId(categoryId), pageable);
        } else if (minPrice != null || maxPrice != null) {
            page = service.findByPriceBetween(minPrice, maxPrice, pageable);
        } else if (title != null && !title.isBlank()) {
            page = service.findByTitleContains(title, pageable);
        } else if (minRating != null) {
            page = service.findByRatingAtLeast(minRating, pageable);
        } else if (discounted) {
            page = service.findDiscounted(pageable);
        } else if (country != null && !country.isBlank()) {
            page = service.findByCountry(country, pageable);
        } else if (city != null && !city.isBlank()) {
            page = service.findByCity(city, pageable);
        } else {
            page = service.findAll(pageable);
        }
        return ResponseEntity.ok(page);
    }
}