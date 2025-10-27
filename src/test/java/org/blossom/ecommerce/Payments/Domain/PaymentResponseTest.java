package org.blossom.ecommerce.Payments.Domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentResponseTest {

    @Test
    void testPaymentResponse_Approved() {
        PaymentResponse response = new PaymentResponse("txn123", PaymentStatus.APPROVED, "Payment successful");
        assertNotNull(response);
        assertEquals("txn123", response.transactionId());
        assertEquals(PaymentStatus.APPROVED, response.status());
        assertEquals("Payment successful", response.message());
        assertTrue(response.approved());
    }

    @Test
    void testPaymentResponse_Rejected() {
        PaymentResponse response = new PaymentResponse("txn456", PaymentStatus.REJECTED, "Insufficient funds");
        assertNotNull(response);
        assertEquals("txn456", response.transactionId());
        assertEquals(PaymentStatus.REJECTED, response.status());
        assertEquals("Insufficient funds", response.message());
        assertFalse(response.approved());
    }


    @Test
    void testEqualsAndHashCode() {
        PaymentResponse response1 = new PaymentResponse("txn1", PaymentStatus.APPROVED, "Msg");
        PaymentResponse response2 = new PaymentResponse("txn1", PaymentStatus.APPROVED, "Msg");
        PaymentResponse response3 = new PaymentResponse("txn2", PaymentStatus.REJECTED, "Msg");

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1, response3);
    }
}