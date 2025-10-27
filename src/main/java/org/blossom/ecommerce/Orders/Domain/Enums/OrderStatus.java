package org.blossom.ecommerce.Orders.Domain.Enums;

public enum OrderStatus {
    CREATED,           // Orden creada pero aún sin confirmar
    WAITING_PAYMENT,   // Pendiente de pago
    PAID,              // Pago confirmado
    PROCESSING,        // Preparando productos (picking, empaquetado)
    WAITING_DELIVERY,  // Esperando despacho (logística asignada)
    DELIVERING,        // En tránsito hacia el cliente
    DELIVERED,         // Entregado con éxito
    CANCELED,          // Cancelada por el usuario o sistema
    REFUNDED           // Reembolsada (post-entrega o por reclamo)
}