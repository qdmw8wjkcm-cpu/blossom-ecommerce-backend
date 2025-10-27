package org.blossom.ecommerce.Users.Infrastructure.Rest.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.blossom.ecommerce.Users.Domain.Enums.Role;
import org.blossom.ecommerce.Users.Application.Dto.CreateUserSpec;
import org.blossom.ecommerce.Users.Application.Dto.NewUser;
import org.blossom.ecommerce.Users.Application.Dto.UpdateUser;
import org.blossom.ecommerce.Users.Application.Service.UserService;
import org.blossom.ecommerce.Users.Domain.Enums.Status;
import org.blossom.ecommerce.Users.Domain.Models.User;
import org.blossom.ecommerce.Users.Domain.ValueObjects.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management endpoints")
public class UserController {

    private final UserService service;

    @Operation(summary = "Create user", description = "Creates a new user profile")
    @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = NewUser.class)))
    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody NewUser dto) {
        var spec = new CreateUserSpec(
                dto.getName(),
                dto.getLastname(),
                dto.getPassword(),
                new Email(dto.getEmail()),
                new DocumentId(dto.getDocument()),
                dto.getPhoneNumber() != null ? new PhoneNumber(dto.getPhoneNumber()) : null,
                dto.getCity(),
                dto.getCountry(),
                dto.getFavoriteCategoriesList() != null
                        ? dto.getFavoriteCategoriesList().stream()
                        .map(id -> new CategoryId(UUID.fromString(id)))
                        .collect(Collectors.toSet())
                        : Set.of(),
                dto.getFavoriteProductsList() != null
                        ? dto.getFavoriteProductsList().stream()
                        .map(id -> new ProductId(UUID.fromString(id)))
                        .collect(Collectors.toSet())
                        : Set.of());

        var saved = service.createProfile(spec).orElseThrow();

        return ResponseEntity.status(201).body(saved);
    }

    @Operation(summary = "Update user", description = "Updates selected fields of a user")
    @ApiResponse(responseCode = "200", description = "Updated")
    @PutMapping("/{id}")
    public ResponseEntity<User> update(
            @Valid @RequestBody UpdateUser update) {
        User updated = service.updateProfile(update);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Find by id", description = "Returns a user by id")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable("id") UUID id) {
        return service.findById(new UserId(id))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "List users", description = "Paginated listing; optional filters by status or role")
    @GetMapping
    public ResponseEntity<Page<User>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Role role
    ) {
        Page<User> result;
        if (status != null) {
            result = service.findAllByStatus(status, page, size);
        } else if (role != null) {
            result = service.findAllByRole(role, page, size);
        } else {
            result = service.findAll(page, size);
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Soft delete user", description = "Marks user as DELETED (soft delete)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable("id") UUID id) {
        service.changeStatus(new UserId(id), Status.INACTIVE);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Add favorite category", description = "Adds a category to user's favorites")
    @PostMapping("/{id}/favorites/categories/{categoryId}")
    public ResponseEntity<Void> addFavoriteCategory(
            @PathVariable("id") UUID userId,
            @PathVariable("categoryId") UUID categoryId) {
        service.addFavoriteCategory(new UserId(userId),new CategoryId(categoryId));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove favorite category", description = "Removes a category from user's favorites")
    @DeleteMapping("/{id}/favorites/categories/{categoryId}")
    public ResponseEntity<Void> removeFavoriteCategory(
            @PathVariable("id") UUID userId,
            @PathVariable("categoryId") UUID categoryId) {
        service.removeFavoriteCategory(new UserId(userId), new CategoryId(categoryId));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get favorite categories", description = "Returns set of favorite category IDs")
    @GetMapping("/{id}/favorites/categories")
    public ResponseEntity<Set<?>> getFavoriteCategories(@PathVariable("id") UUID userId) {
        return ResponseEntity.ok(service.getFavoriteCategories(new UserId(userId)));
    }

    @Operation(summary = "Add favorite product", description = "Adds a product to user's favorites")
    @PostMapping("/{id}/favorites/products/{productId}")
    public ResponseEntity<Void> addFavoriteProduct(
            @PathVariable("id") UUID userId,
            @PathVariable("productId") UUID productId) {
        service.addFavoriteProduct(new UserId(userId), new ProductId(productId));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove favorite product", description = "Removes a product from user's favorites")
    @DeleteMapping("/{id}/favorites/products/{productId}")
    public ResponseEntity<Void> removeFavoriteProduct(
            @PathVariable("id") UUID userId,
            @PathVariable("productId") UUID productId) {
        service.removeFavoriteProduct( new UserId(userId), new ProductId(productId));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get favorite products", description = "Returns set of favorite product IDs")
    @GetMapping("/{id}/favorites/products")
    public ResponseEntity<Set<?>> getFavoriteProducts(@PathVariable("id") UUID userId) {
        return ResponseEntity.ok(service.getFavoriteProducts(new UserId(userId)));
    }
}