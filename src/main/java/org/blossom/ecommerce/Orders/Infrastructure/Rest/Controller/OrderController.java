package org.blossom.ecommerce.Orders.Infrastructure.Rest.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.blossom.ecommerce.Orders.Domain.Enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import org.blossom.ecommerce.Orders.Application.Service.OrderService;
import org.blossom.ecommerce.Orders.Domain.Enums.PaymentMethod;
import org.blossom.ecommerce.Orders.Domain.Models.Order;
import org.blossom.ecommerce.Orders.Domain.spec.*;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.*;
import org.blossom.ecommerce.Orders.Infrastructure.Rest.Dto.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management endpoints")
public class OrderController {

    private final OrderService service;

    @Operation(summary = "Create order")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody NewOrder dto) {
        var spec = new CreateOrderSpec(
                new UserId(dto.userId()),
                PaymentMethod.valueOf(dto.paymentMethod().toUpperCase()),
                dto.items().stream()
                        .map(i -> new CreateOrderSpec.Item(new ProductId(i.productId()), i.quantity()))
                        .toList()
        );
        var saved = service.create(spec).orElseThrow();
        return ResponseEntity.status(201).body(saved.getId().value());
    }

    @Operation(summary = "Add item")
    @PostMapping("/{orderId}/items")
    public ResponseEntity<Order> addItem(@PathVariable UUID orderId, @Valid @RequestBody AddItemRequest dto) {
        var updated = service.addItem(new AddOrderItem(new OrderId(orderId), new ProductId(dto.productId()), dto.quantity()));
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Remove item")
    @DeleteMapping("/{orderId}/items/{productId}")
    public ResponseEntity<Order> removeItem(
            @PathVariable UUID orderId,
            @PathVariable UUID productId,
            @RequestParam(name = "quantity") int quantity
    ) {
        var updated = service.removeItem(new RemoveOrderItem(new OrderId(orderId), new ProductId(productId), quantity));
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Mark as delivered")
    @PostMapping("/{orderId}/deliver")
    public ResponseEntity<Order> deliver(@PathVariable UUID orderId) {
        var updated = service.markAsDelivered(new OrderId(orderId));
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Refund order")
    @PostMapping("/{orderId}/refund")
    public ResponseEntity<Order> refund(@PathVariable UUID orderId, @Valid @RequestBody ApplyDiscountRequest dto) {
        var updated = service.refund(new RefundOrder(new OrderId(orderId), dto.amount()));
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Update order (change payment method while pending)")
    @PutMapping("/{orderId}")
    public ResponseEntity<Order> update(@PathVariable UUID orderId, @Valid @RequestBody UpdateOrderRequest dto) {
        var updated = service.update(new UpdateOrder(
                new OrderId(orderId),
                dto.paymentMethod() != null ? PaymentMethod.valueOf(dto.paymentMethod().toUpperCase()) : null
        ));
        return ResponseEntity.ok(updated);
    }
    @Operation(summary = "Get order by ID")
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> findById(@PathVariable UUID orderId) {
        return service.findById(new OrderId(orderId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "List all orders")
    @GetMapping
    public ResponseEntity<Page<Order>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) String status
    ) {
        var pageable = PageRequest.of(page, size);
        if (userId != null)
            return ResponseEntity.ok(service.findByUser(new UserId(userId), pageable));

        if (status != null)
            return ResponseEntity.ok(service.findByStatus(OrderStatus.valueOf(status.toUpperCase()), pageable));

        return ResponseEntity.ok(service.findAll(pageable));
    }
}