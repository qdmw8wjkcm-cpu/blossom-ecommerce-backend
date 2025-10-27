package org.blossom.ecommerce.Orders.Domain.spec;

import org.blossom.ecommerce.Orders.Domain.Enums.PaymentMethod;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.OrderId;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PayOrderTest {

    @Test
    void testPayOrder_ConstructionAndAccessors() {
        OrderId orderId = new OrderId(UUID.randomUUID());
        PaymentMethod method = PaymentMethod.CREDIT_CARD;
        String transactionId = "txn_12345";
        Instant paidAt = Instant.now();

        PayOrder spec = new PayOrder(orderId, method, transactionId, paidAt);
        assertEquals(orderId, spec.orderId());
        assertEquals(method, spec.method());
        assertEquals(transactionId, spec.transactionId());
        assertEquals(paidAt, spec.paidAt());
    }

    @Test
    void testEqualsAndHashCode() {
        OrderId orderId = new OrderId(UUID.randomUUID());
        PaymentMethod method = PaymentMethod.CREDIT_CARD;
        String transactionId = "txn_12345";
        Instant paidAt = Instant.now();

        PayOrder spec1 = new PayOrder(orderId, method, transactionId, paidAt);
        PayOrder spec2 = new PayOrder(orderId, method, transactionId, paidAt);

        assertEquals(spec1, spec2);
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }
}