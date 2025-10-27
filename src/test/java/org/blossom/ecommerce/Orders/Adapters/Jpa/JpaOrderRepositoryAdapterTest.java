package org.blossom.ecommerce.Orders.Adapters.Jpa;

import org.blossom.ecommerce.Orders.Adapters.Mappers.OrderMapper;
import org.blossom.ecommerce.Orders.Domain.Enums.OrderStatus;
import org.blossom.ecommerce.Orders.Domain.Enums.PaymentMethod;
import org.blossom.ecommerce.Orders.Domain.Models.Order;
import org.blossom.ecommerce.Orders.Domain.Models.OrderItem;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.OrderId;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.ProductId;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({JpaOrderRepositoryAdapter.class, OrderMapper.class}) // Import the adapter and mapper
@ActiveProfiles("test")
class JpaOrderRepositoryAdapterTest {

    @Autowired
    private JpaOrderRepositoryAdapter adapter;

    @Autowired
    private OrderJpaRepository jpaRepository; // To verify direct DB state if needed

    private Order order1;
    private Order order2;
    private UserId userId1;
    private UserId userId2;
    private ProductId productId1;
    private ProductId productId2;

    @BeforeEach
    void setUp() {
        // Clear database before each test
        jpaRepository.deleteAll();

        userId1 = new UserId(UUID.randomUUID());
        userId2 = new UserId(UUID.randomUUID());
        productId1 = new ProductId(UUID.randomUUID());
        productId2 = new ProductId(UUID.randomUUID());

        // Create Order 1
        order1 = Order.create(userId1, PaymentMethod.CREDIT_CARD);
        order1.addItem(productId1, 2);
        order1.recalculateTotals();
        order1 = Order.rehydrate(
                new OrderId(UUID.randomUUID()), order1.getUserId(), order1.getItems(),
                order1.getTotal(), order1.getDiscount(), order1.getCreatedAt(), order1.getUpdatedAt(),
                order1.getStatus(), order1.getPaymentMethod()
        );

        // Create Order 2
        order2 = Order.create(userId2, PaymentMethod.PAYPAL);
        order2.addItem(productId2, 1);
        order2.recalculateTotals();
        order2 = Order.rehydrate(
                new OrderId(UUID.randomUUID()), order2.getUserId(), order2.getItems(),
                order2.getTotal(), order2.getDiscount(), order2.getCreatedAt(), order2.getUpdatedAt(),
                order2.getStatus(), order2.getPaymentMethod()
        );
    }

    @Test
    void testSaveOrder() {
        Order savedOrder = adapter.save(order1);

        assertNotNull(savedOrder.getId());
        assertEquals(order1.getUserId(), savedOrder.getUserId());
        assertEquals(order1.getTotal(), savedOrder.getTotal());
        assertEquals(1, savedOrder.getItems().size());

        // Verify it's in the database
        assertTrue(jpaRepository.findById(savedOrder.getId().value()).isPresent());
    }

    @Test
    void testFindById() {
        adapter.save(order1);

        Optional<Order> foundOrder = adapter.findById(order1.getId());

        assertTrue(foundOrder.isPresent());
        assertEquals(order1.getId(), foundOrder.get().getId());
        assertEquals(order1.getUserId(), foundOrder.get().getUserId());
    }

    @Test
    void testFindAll() {
        adapter.save(order1);
        adapter.save(order2);

        PageRequest pageRequest = PageRequest.of(0, 10);
        var ordersPage = adapter.findAll(pageRequest);

        assertEquals(2, ordersPage.getTotalElements());
        assertEquals(1, ordersPage.getTotalPages());
        assertTrue(ordersPage.getContent().stream().anyMatch(o -> o.getId().equals(order1.getId())));
        assertTrue(ordersPage.getContent().stream().anyMatch(o -> o.getId().equals(order2.getId())));
    }

    @Test
    void testFindByUser() {
        adapter.save(order1);
        adapter.save(order2);

        PageRequest pageRequest = PageRequest.of(0, 10);
        var ordersPage = adapter.findByUser(userId1, pageRequest);

        assertEquals(1, ordersPage.getTotalElements());
        assertEquals(order1.getId(), ordersPage.getContent().get(0).getId());
    }

    @Test
    void testFindByStatus() {
        order1.markAsPaid(); // Change status to PROCESSING
        adapter.save(order1);
        adapter.save(order2);

        PageRequest pageRequest = PageRequest.of(0, 10);
        var ordersPage = adapter.findByStatus(OrderStatus.PROCESSING, pageRequest);

        assertEquals(1, ordersPage.getTotalElements());
        assertEquals(order1.getId(), ordersPage.getContent().get(0).getId());
    }

    @Test
    void testFindByDateRange() {
        Instant before = Instant.now().minusSeconds(100);
        Instant after = Instant.now().plusSeconds(100);

        order1 = Order.rehydrate(
                new OrderId(UUID.randomUUID()), userId1, List.of(new OrderItem(productId1, 1)),
                BigDecimal.TEN, BigDecimal.ZERO, Instant.now().minusSeconds(50), Instant.now().minusSeconds(50),
                OrderStatus.WAITING_PAYMENT, PaymentMethod.CREDIT_CARD
        );
        adapter.save(order1);

        order2 = Order.rehydrate(
                new OrderId(UUID.randomUUID()), userId2, List.of(new OrderItem(productId2, 1)),
                BigDecimal.ONE, BigDecimal.ZERO, Instant.now().plusSeconds(50), Instant.now().plusSeconds(50),
                OrderStatus.WAITING_PAYMENT, PaymentMethod.PAYPAL
        );
        adapter.save(order2);

        PageRequest pageRequest = PageRequest.of(0, 10);
        var ordersPage = adapter.findByDateRange(before, after, pageRequest);

        assertEquals(1, ordersPage.getTotalElements());
        assertEquals(order1.getId(), ordersPage.getContent().get(0).getId());
    }
}