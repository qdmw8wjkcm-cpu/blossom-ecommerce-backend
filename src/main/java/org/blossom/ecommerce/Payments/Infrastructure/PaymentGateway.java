package org.blossom.ecommerce.Payments.Infrastructure;

import org.blossom.ecommerce.Payments.Domain.PaymentGatewayResponse;

import java.util.UUID;

public interface PaymentGateway {
    PaymentGatewayResponse charge(UUID orderId);
}