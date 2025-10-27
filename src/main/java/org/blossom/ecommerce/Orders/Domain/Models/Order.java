package org.blossom.ecommerce.Orders.Domain.Models;

import org.blossom.ecommerce.Orders.Domain.Enums.OrderStatus;
import org.blossom.ecommerce.Orders.Domain.Enums.PaymentMethod;
import org.blossom.ecommerce.Orders.Domain.Exceptions.DomainValidationException;
import org.blossom.ecommerce.Orders.Domain.Enums.OrderMessage;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.OrderId;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.ProductId;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.UserId;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

public class Order {

    private OrderId id;
    private UserId userId;
    private final List<OrderItem> items = new ArrayList<>();
    private BigDecimal total;
    private BigDecimal discount;
    private Instant createdAt;
    private Instant updatedAt;
    private OrderStatus status;
    private PaymentMethod paymentMethod;

    private Order() {}

    public static Order rehydrate(
            OrderId id, UserId userId, List<OrderItem> items,
            BigDecimal total, BigDecimal discount,
            Instant createdAt, Instant updatedAt,
            OrderStatus status, PaymentMethod paymentMethod
    ) {
        Order o = new Order();
        o.id = id;
        o.userId = userId;
        o.items.addAll(items != null ? items : List.of());
        o.total = total != null ? total : BigDecimal.ZERO;
        o.discount = discount != null ? discount : BigDecimal.ZERO;
        o.createdAt = createdAt;
        o.updatedAt = updatedAt;
        o.status = status;
        o.paymentMethod = paymentMethod;
        return o;
    }

    public static Order create(UserId userId, PaymentMethod paymentMethod) {
        if (userId == null) throw new DomainValidationException(OrderMessage.USER_REQUIRED.getMessage());
        if (paymentMethod == null) throw new DomainValidationException(OrderMessage.PAYMENT_REQUIRED.getMessage());

        Order o = new Order();
        o.id = null;
        o.userId = userId;
        o.total = BigDecimal.ZERO;
        o.discount = BigDecimal.ZERO;
        Instant now = Instant.now();
        o.createdAt = now;
        o.updatedAt = now;
        o.status = OrderStatus.WAITING_PAYMENT;
        o.paymentMethod = paymentMethod;
        return o;
    }

    public void assertMutable() {
        if (status == OrderStatus.DELIVERED || status == OrderStatus.CANCELED || status == OrderStatus.REFUNDED) {
            throw new DomainValidationException(OrderMessage.INVALID_ITEM_QUANTITY.getMessage());
        }
    }

    public void addItem(ProductId productId, int quantity) {
        if (productId == null || quantity <= 0)
            throw new DomainValidationException(OrderMessage.INVALID_ITEM_QUANTITY.getMessage());
        assertMutable();

        for (int i = 0; i < items.size(); i++) {
            var it = items.get(i);
            if (it.getProductId().equals(productId)) {
                items.set(i, new OrderItem(productId, it.getQuantity() + quantity));
                recalculateTotals();
                touch();
                return;
            }
        }
        items.add(new OrderItem(productId, quantity));
        recalculateTotals();
        touch();
    }

    public void removeItem(ProductId productId, int quantity) {
        if (productId == null || quantity <= 0)
            throw new DomainValidationException(OrderMessage.INVALID_ITEM_QUANTITY.getMessage());
        assertMutable();

        for (int i = 0; i < items.size(); i++) {
            var it = items.get(i);
            if (it.getProductId().equals(productId)) {
                int remaining = it.getQuantity() - quantity;
                if (remaining > 0) {
                    items.set(i, new OrderItem(productId, remaining));
                } else if (remaining == 0) {
                    items.remove(i);
                } else {
                    throw new DomainValidationException(OrderMessage.INVALID_ITEM_QUANTITY.getMessage());
                }
                recalculateTotals();
                touch();
                return;
            }
        }
        throw new DomainValidationException(OrderMessage.INVALID_ITEM_QUANTITY.getMessage());
    }

    public void recalculateTotals() {
        int qty = items.stream().mapToInt(OrderItem::getQuantity).sum();
        this.total = BigDecimal.valueOf(qty);
        if (this.discount == null) this.discount = BigDecimal.ZERO;
    }

    public void applyDiscount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0)
            throw new DomainValidationException(OrderMessage.INVALID_DISCOUNT.getMessage());
        this.discount = amount.min(this.total);
        this.total = this.total.subtract(this.discount);
        touch();
    }

    public void waitingPayment() {
        if (this.status != OrderStatus.CREATED)
            throw new DomainValidationException(OrderMessage.INVALID_STATE_TRANSITION.getMessage());

        this.status = OrderStatus.WAITING_PAYMENT;
        touch();
    }

    public void changePaymentMethod(PaymentMethod method) {
        if (method == null) return;
        if (status != OrderStatus.WAITING_PAYMENT)
            throw new DomainValidationException(OrderMessage.INVALID_STATE_TRANSITION.getMessage());
        this.paymentMethod = method;
        touch();
    }

    public void markAsPaid() {
        if (status != OrderStatus.WAITING_PAYMENT)
            throw new DomainValidationException(OrderMessage.INVALID_STATE_TRANSITION.getMessage());
        status = OrderStatus.PROCESSING;
        touch();
    }

    public void markAsDelivered(Instant when) {
        if (status != OrderStatus.DELIVERING && status != OrderStatus.WAITING_DELIVERY && status != OrderStatus.PROCESSING)
            throw new DomainValidationException(OrderMessage.INVALID_STATE_TRANSITION.getMessage());
        status = OrderStatus.DELIVERED;
        touch();
    }

    public void cancel() {
        if (status == OrderStatus.DELIVERED)
            throw new DomainValidationException(OrderMessage.CANNOT_CANCEL_DELIVERED.getMessage());
        status = OrderStatus.CANCELED;
        touch();
    }

    public void refund(BigDecimal amount) {
        if (status != OrderStatus.DELIVERED)
            throw new DomainValidationException(OrderMessage.INVALID_REFUND.getMessage());
        status = OrderStatus.REFUNDED;
        touch();
    }

    private void touch() { this.updatedAt = Instant.now(); }

    // Getters
    public OrderId getId() { return id; }
    public UserId getUserId() { return userId; }
    public List<OrderItem> getItems() { return List.copyOf(items); }
    public BigDecimal getTotal() { return total; }
    public BigDecimal getDiscount() { return discount; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public OrderStatus getStatus() { return status; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
}