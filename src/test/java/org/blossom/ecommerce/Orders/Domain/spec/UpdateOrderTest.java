package org.blossom.ecommerce.Orders.Domain.spec;

import org.blossom.ecommerce.Orders.Domain.Enums.PaymentMethod;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.OrderId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UpdateOrderTest {

    @Test
    void testUpdateOrder_ConstructionAndAccessors() {
        OrderId orderId = new OrderId(UUID.randomUUID());
        PaymentMethod paymentMethod = PaymentMethod.PAYPAL;

        UpdateOrder spec = new UpdateOrder(orderId, paymentMethod);

        assertEquals(orderId, spec.id());
        assertEquals(paymentMethod, spec.paymentMethod());
    }

    @Test
    void testEqualsAndHashCode() {
        OrderId orderId = new OrderId(UUID.randomUUID());
        PaymentMethod paymentMethod = PaymentMethod.PAYPAL;

        UpdateOrder spec1 = new UpdateOrder(orderId, paymentMethod);
        UpdateOrder spec2 = new UpdateOrder(orderId, paymentMethod);

        // Assert
        assertEquals(spec1, spec2);
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }
}