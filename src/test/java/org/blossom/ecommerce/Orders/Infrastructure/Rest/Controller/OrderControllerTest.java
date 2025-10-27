package org.blossom.ecommerce.Orders.Infrastructure.Rest.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.blossom.ecommerce.Orders.Application.Service.OrderService;
import org.blossom.ecommerce.Orders.Domain.Enums.OrderStatus;
import org.blossom.ecommerce.Orders.Domain.Enums.PaymentMethod;
import org.blossom.ecommerce.Orders.Domain.Models.Order;
import org.blossom.ecommerce.Orders.Domain.Models.OrderItem;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.OrderId;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.ProductId;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.UserId;
import org.blossom.ecommerce.Orders.Infrastructure.Rest.Dto.AddItemRequest;
import org.blossom.ecommerce.Orders.Infrastructure.Rest.Dto.NewOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@ActiveProfiles("test")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    private OrderId testOrderId;
    private UserId testUserId;
    private ProductId testProductId;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        testOrderId = new OrderId(UUID.randomUUID());
        testUserId = new UserId(UUID.randomUUID());
        testProductId = new ProductId(UUID.randomUUID());

        testOrder = Order.rehydrate(
                testOrderId,
                testUserId,
                List.of(new OrderItem(testProductId, 1)),
                BigDecimal.valueOf(100.00),
                BigDecimal.ZERO,
                Instant.now(),
                Instant.now(),
                OrderStatus.WAITING_PAYMENT,
                PaymentMethod.CREDIT_CARD
        );
    }

    @Test
    void testCreateOrder_Success() throws Exception {
        NewOrder.Item newItem = new NewOrder.Item(testProductId.value(), "Product Name", BigDecimal.TEN, 2);
        NewOrder newOrderDto = new NewOrder(testUserId.value(), "CREDIT_CARD", List.of(newItem));

        when(orderService.create(any())).thenReturn(Optional.of(testOrder));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newOrderDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string(testOrderId.value().toString()));

        verify(orderService, times(1)).create(any());
    }

    @Test
    void testAddItem_Success() throws Exception {
        AddItemRequest addItemRequest = new AddItemRequest(testProductId.value(), "Product Name", BigDecimal.TEN, 1);

        when(orderService.addItem(any())).thenReturn(testOrder);

        mockMvc.perform(post("/api/orders/{orderId}/items", testOrderId.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addItemRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.value", is(testOrderId.value().toString())));

        verify(orderService, times(1)).addItem(any());
    }

    @Test
    void testGetOrderById_Found() throws Exception {
        when(orderService.findById(testOrderId)).thenReturn(Optional.of(testOrder));

        mockMvc.perform(get("/api/orders/{orderId}", testOrderId.value()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.value", is(testOrderId.value().toString())));

        verify(orderService, times(1)).findById(testOrderId);
    }

    @Test
    void testGetOrderById_NotFound() throws Exception {
        when(orderService.findById(any(OrderId.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/orders/{orderId}", UUID.randomUUID()))
                .andExpect(status().isNotFound());

        verify(orderService, times(1)).findById(any(OrderId.class));
    }

    @Test
    void testListAllOrders() throws Exception {
        when(orderService.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(testOrder), PageRequest.of(0, 20), 1));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id.value", is(testOrderId.value().toString())));

        verify(orderService, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void testRemoveItem_Success() throws Exception {
        when(orderService.removeItem(any())).thenReturn(testOrder);

        mockMvc.perform(delete("/api/orders/{orderId}/items/{productId}", testOrderId.value(), testProductId.value())
                        .param("quantity", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.value", is(testOrderId.value().toString())));

        verify(orderService, times(1)).removeItem(any());
    }

    @Test
    void testDeliverOrder_Success() throws Exception {
        when(orderService.markAsDelivered(any(OrderId.class))).thenReturn(testOrder);

        mockMvc.perform(post("/api/orders/{orderId}/deliver", testOrderId.value()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.value", is(testOrderId.value().toString())));

        verify(orderService, times(1)).markAsDelivered(any(OrderId.class));
    }

    // Add more tests for update, refund, and other list filters
}
