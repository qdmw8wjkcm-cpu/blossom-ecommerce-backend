package org.blossom.ecommerce.Payments.Domain;

public record PaymentResponse(
        String transactionId,
        PaymentStatus status,
        String message
) {
    public boolean approved() {
        return status == PaymentStatus.APPROVED;
    }
}