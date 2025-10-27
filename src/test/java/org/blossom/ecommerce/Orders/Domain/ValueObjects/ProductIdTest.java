package org.blossom.ecommerce.Orders.Domain.ValueObjects;

import org.blossom.ecommerce.Users.Domain.Enums.ErrorMessage;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductIdTest {

    @Test
    void testCreateProductId_Success() {
        UUID uuid = UUID.randomUUID();
        ProductId productId = new ProductId(uuid);
        assertNotNull(productId);
        assertEquals(uuid, productId.value());
    }

    @Test
    void testCreateProductId_NullValue_ShouldThrowException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            new ProductId(null);
        });
        // The first null check will trigger
        assertEquals(ErrorMessage.PRODUCT_NULL.getMessage(), exception.getMessage());
    }

    @Test
    void testProductIdStaticMethod_ShouldReturnNonNullProductId() {
        ProductId productId = ProductId.productId();
        assertNotNull(productId);
        assertNotNull(productId.value());
    }

    @Test
    void testFromString_ValidUUIDString_Success() {
        String uuidString = UUID.randomUUID().toString();
        ProductId productId = ProductId.fromString(uuidString);
        assertNotNull(productId);
        assertEquals(UUID.fromString(uuidString), productId.value());
    }

    @Test
    void testFromString_InvalidUUIDString_ShouldThrowException() {
        String invalidUuidString = "not-a-uuid";
        assertThrows(IllegalArgumentException.class, () -> {
            ProductId.fromString(invalidUuidString);
        });
    }
}