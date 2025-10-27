package org.blossom.ecommerce.Payments.Domain;

public record PaymentGatewayResponse(boolean approved, String transactionId) {}