package org.blossom.ecommerce.Orders.Application.Service;

import org.blossom.ecommerce.Orders.Domain.Enums.OrderStatus;
import org.blossom.ecommerce.Orders.Domain.Enums.PaymentMethod;
import org.blossom.ecommerce.Orders.Domain.Models.Order;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.OrderId;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.ProductId;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.UserId;
import org.blossom.ecommerce.Orders.Domain.spec.AddOrderItem;
import org.blossom.ecommerce.Orders.Domain.spec.CreateOrderSpec;
import org.blossom.ecommerce.Orders.Infrastructure.Ports.Out.OrderRepository;
import org.blossom.ecommerce.Payments.Infrastructure.PaymentGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @MockBean
    private OrderRepository repo;

    @MockBean
    private PaymentGateway paymentGateway;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private OrderService orderService;

    private Order testOrder;
    private final UserId defaultUserId = new UserId(UUID.randomUUID());
    private final OrderId defaultOrderId = new OrderId(UUID.randomUUID());

    @BeforeEach
    void setUp() {
        testOrder = Order.create(defaultUserId, PaymentMethod.CREDIT_CARD);
        testOrder = Order.rehydrate(defaultOrderId, defaultUserId, testOrder.getItems(), testOrder.getTotal(),
                testOrder.getDiscount(), testOrder.getCreatedAt(), testOrder.getUpdatedAt(),
                testOrder.getStatus(), testOrder.getPaymentMethod());
    }

    @Test
    void testCreateOrder_Success() {
        CreateOrderSpec spec = new CreateOrderSpec(
                defaultUserId,
                PaymentMethod.CREDIT_CARD,
                List.of(new CreateOrderSpec.Item(new ProductId(UUID.randomUUID()), 3))
        );

        when(repo.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Order> createdOrderOpt = orderService.create(spec);

        assertTrue(createdOrderOpt.isPresent());
        Order createdOrder = createdOrderOpt.get();
        assertEquals(1, createdOrder.getItems().size());
        assertEquals(defaultUserId, createdOrder.getUserId());

        verify(repo, times(1)).save(any(Order.class));
    }

    @Test
    void testFindById_OrderExists() {
        when(repo.findById(defaultOrderId)).thenReturn(Optional.of(testOrder));

        Optional<Order> foundOrder = orderService.findById(defaultOrderId);

        assertTrue(foundOrder.isPresent());
        assertEquals(defaultOrderId, foundOrder.get().getId());
        verify(repo, times(1)).findById(defaultOrderId);
    }

    @Test
    void testFindById_OrderNotFound() {
        when(repo.findById(defaultOrderId)).thenReturn(Optional.empty());

        Optional<Order> foundOrder = orderService.findById(defaultOrderId);

        assertFalse(foundOrder.isPresent());
        verify(repo, times(1)).findById(defaultOrderId);
    }

    @Test
    void testAddItem_Success() {
        when(repo.findById(defaultOrderId)).thenReturn(Optional.of(testOrder));
        when(repo.save(any(Order.class))).thenReturn(testOrder);

        AddOrderItem cmd = new AddOrderItem(defaultOrderId, new ProductId(UUID.randomUUID()), 1);
        Order updatedOrder = orderService.addItem(cmd);

        assertNotNull(updatedOrder);
        assertEquals(1, updatedOrder.getItems().size());
        verify(repo, times(1)).findById(defaultOrderId);
        verify(repo, times(1)).save(testOrder);
    }

    @Test
    void testCancelOrder_Success() {
        when(repo.findById(defaultOrderId)).thenReturn(Optional.of(testOrder));
        when(repo.save(any(Order.class))).thenReturn(testOrder);

        orderService.delete(defaultOrderId);

        assertEquals(OrderStatus.CANCELED, testOrder.getStatus());
        verify(repo, times(1)).findById(defaultOrderId);
        verify(repo, times(1)).save(testOrder);
    }
}