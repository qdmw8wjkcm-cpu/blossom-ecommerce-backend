package org.blossom.ecommerce.Orders.Domain.Models;

import org.blossom.ecommerce.Orders.Domain.Enums.OrderStatus;
import org.blossom.ecommerce.Orders.Domain.Enums.PaymentMethod;
import org.blossom.ecommerce.Orders.Domain.Exceptions.DomainValidationException;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.ProductId;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


class OrderTest {

    private final UserId defaultUserId = new UserId(UUID.randomUUID());
    private final ProductId defaultProductId = new ProductId(UUID.randomUUID());

    @Test
    void testCreateOrder_Success() {
        Order order = Order.create(defaultUserId, PaymentMethod.CREDIT_CARD);

        assertNotNull(order);
        assertEquals(defaultUserId, order.getUserId());
        assertEquals(PaymentMethod.CREDIT_CARD, order.getPaymentMethod());
        assertEquals(OrderStatus.WAITING_PAYMENT, order.getStatus());
        assertEquals(BigDecimal.ZERO, order.getTotal());
        assertTrue(order.getItems().isEmpty());
    }

    @Test
    void testCreateOrder_NullUserId_ShouldThrowException() {
        assertThrows(DomainValidationException.class, () -> {
            Order.create(null, PaymentMethod.CREDIT_CARD);
        });
    }

    @Test
    void testAddItem_AddNewItem() {
        Order order = Order.create(defaultUserId, PaymentMethod.CREDIT_CARD);
        order.addItem(defaultProductId, 2);

        assertEquals(1, order.getItems().size());
        assertEquals(2, order.getItems().get(0).getQuantity());
        assertEquals(new BigDecimal("2"), order.getTotal());
    }

    @Test
    void testAddItem_AddExistingItem() {
        Order order = Order.create(defaultUserId, PaymentMethod.CREDIT_CARD);
        order.addItem(defaultProductId, 2);
        order.addItem(defaultProductId, 3);

        assertEquals(1, order.getItems().size());
        assertEquals(5, order.getItems().get(0).getQuantity());
        assertEquals(new BigDecimal("5"), order.getTotal());
    }

    @Test
    void testAddItem_ImmutableOrder_ShouldThrowException() {
        Order order = Order.rehydrate(null, defaultUserId, null, null, null, null, null, OrderStatus.DELIVERED, null);
        assertThrows(DomainValidationException.class, () -> {
            order.addItem(defaultProductId, 1);
        });
    }

    @Test
    void testRemoveItem_PartialQuantity() {
        Order order = Order.create(defaultUserId, PaymentMethod.CREDIT_CARD);
        order.addItem(defaultProductId, 5);
        order.removeItem(defaultProductId, 2);

        assertEquals(1, order.getItems().size());
        assertEquals(3, order.getItems().get(0).getQuantity());
        assertEquals(new BigDecimal("3"), order.getTotal());
    }

    @Test
    void testRemoveItem_FullQuantity() {
        Order order = Order.create(defaultUserId, PaymentMethod.CREDIT_CARD);
        order.addItem(defaultProductId, 5);
        order.removeItem(defaultProductId, 5);

        assertTrue(order.getItems().isEmpty());
        assertEquals(BigDecimal.ZERO, order.getTotal());
    }

    @Test
    void testRemoveItem_MoreThanAvailable_ShouldThrowException() {
        Order order = Order.create(defaultUserId, PaymentMethod.CREDIT_CARD);
        order.addItem(defaultProductId, 2);

        assertThrows(DomainValidationException.class, () -> {
            order.removeItem(defaultProductId, 3);
        });
    }

    @Test
    void testApplyDiscount_Success() {
        Order order = Order.create(defaultUserId, PaymentMethod.CREDIT_CARD);
        order.addItem(defaultProductId, 10); // Total = 10
        order.applyDiscount(new BigDecimal("2.5"));

        assertEquals(new BigDecimal("2.5"), order.getDiscount());
        assertEquals(new BigDecimal("7.5"), order.getTotal());
    }

    @Test
    void testApplyDiscount_LargerThanTotal() {
        Order order = Order.create(defaultUserId, PaymentMethod.CREDIT_CARD);
        order.addItem(defaultProductId, 10); // Total = 10
        order.applyDiscount(new BigDecimal("15"));

        assertEquals(new BigDecimal("10"), order.getDiscount());
        assertEquals(BigDecimal.ZERO, order.getTotal());
    }

    @Test
    void testMarkAsPaid_ValidTransition() {
        Order order = Order.create(defaultUserId, PaymentMethod.CREDIT_CARD);
        order.markAsPaid();
        assertEquals(OrderStatus.PROCESSING, order.getStatus());
    }

    @Test
    void testMarkAsPaid_InvalidTransition_ShouldThrowException() {
        Order order = Order.create(defaultUserId, PaymentMethod.CREDIT_CARD);
        order.markAsPaid();
        assertThrows(DomainValidationException.class, order::markAsPaid);
    }

    @Test
    void testCancel_ValidTransition() {
        Order order = Order.create(defaultUserId, PaymentMethod.CREDIT_CARD);
        order.cancel();
        assertEquals(OrderStatus.CANCELED, order.getStatus());
    }

    @Test
    void testCancel_DeliveredOrder_ShouldThrowException() {
        Order order = Order.rehydrate(null, defaultUserId, null, null, null, null, null, OrderStatus.DELIVERED, null);
        assertThrows(DomainValidationException.class, order::cancel);
    }
}