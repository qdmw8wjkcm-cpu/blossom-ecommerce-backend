package org.blossom.ecommerce.Orders.Domain.spec;

import org.blossom.ecommerce.Orders.Domain.Enums.PaymentMethod;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.ProductId;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CreateOrderSpecTest {

    @Test
    void testCreateOrderSpec_ConstructionAndAccessors() {
        UserId userId = new UserId(UUID.randomUUID());
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;
        ProductId productId = new ProductId(UUID.randomUUID());
        CreateOrderSpec.Item item = new CreateOrderSpec.Item(productId, 2);
        List<CreateOrderSpec.Item> items = List.of(item);

        CreateOrderSpec spec = new CreateOrderSpec(userId, paymentMethod, items);

        assertEquals(userId, spec.userId());
        assertEquals(paymentMethod, spec.paymentMethod());
        assertEquals(items, spec.items());
        assertEquals(1, spec.items().size());
        assertEquals(productId, spec.items().get(0).productId());
        assertEquals(2, spec.items().get(0).quantity());
    }

    @Test
    void testEqualsAndHashCode() {
        UserId userId = new UserId(UUID.randomUUID());
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;
        ProductId productId = new ProductId(UUID.randomUUID());
        CreateOrderSpec.Item item = new CreateOrderSpec.Item(productId, 2);
        List<CreateOrderSpec.Item> items = List.of(item);

        CreateOrderSpec spec1 = new CreateOrderSpec(userId, paymentMethod, items);
        CreateOrderSpec spec2 = new CreateOrderSpec(userId, paymentMethod, items);

        assertEquals(spec1, spec2);
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }
}