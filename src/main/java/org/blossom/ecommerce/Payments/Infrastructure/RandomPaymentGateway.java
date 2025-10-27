package org.blossom.ecommerce.Payments.Infrastructure;

import org.blossom.ecommerce.Payments.Domain.PaymentGatewayResponse;
import org.springframework.stereotype.Component;
import java.util.Random;
import java.util.UUID;

@Component
public class RandomPaymentGateway implements PaymentGateway {
    private final Random random = new Random();

    @Override
    public PaymentGatewayResponse charge(UUID orderId) {
        boolean approved = random.nextBoolean();
        String tx = UUID.randomUUID().toString();
        System.out.println((approved ? "✅" : "❌") + " Payment simulation for order " + orderId + " | tx=" + tx);
        return new PaymentGatewayResponse(approved, tx);
    }
}