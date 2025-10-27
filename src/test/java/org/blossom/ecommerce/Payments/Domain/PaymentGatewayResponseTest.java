package org.blossom.ecommerce.Payments.Domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentGatewayResponseTest {

    @Test
    void testPaymentGatewayResponse_Approved() {
        PaymentGatewayResponse response = new PaymentGatewayResponse(true, "gateway_txn_123");
        assertNotNull(response);
        assertTrue(response.approved());
        assertEquals("gateway_txn_123", response.transactionId());
    }

    @Test
    void testPaymentGatewayResponse_Rejected() {
        PaymentGatewayResponse response = new PaymentGatewayResponse(false, "gateway_txn_456");
        assertNotNull(response);
        assertFalse(response.approved());
        assertEquals("gateway_txn_456", response.transactionId());
    }

    @Test
    void testEqualsAndHashCode() {
        PaymentGatewayResponse response1 = new PaymentGatewayResponse(true, "txn1");
        PaymentGatewayResponse response2 = new PaymentGatewayResponse(true, "txn1");
        PaymentGatewayResponse response3 = new PaymentGatewayResponse(false, "txn2");

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1, response3);
    }
}