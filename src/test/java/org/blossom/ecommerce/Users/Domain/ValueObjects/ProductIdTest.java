package org.blossom.ecommerce.Users.Domain.ValueObjects;

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
        assertEquals(ErrorMessage.PRODUCT_NULL.getMessage(), exception.getMessage());
    }

    @Test
    void testProductIdStaticMethod_ShouldReturnNonNull() {
        ProductId productId = ProductId.productId();
        assertNotNull(productId);
        assertNotNull(productId.value());
    }

    @Test
    void testFromString_ValidString_Success() {
        String uuidString = UUID.randomUUID().toString();
        ProductId productId = ProductId.fromString(uuidString);
        assertNotNull(productId);
        assertEquals(UUID.fromString(uuidString), productId.value());
    }

    @Test
    void testFromString_InvalidString_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            ProductId.fromString("invalid-uuid");
        });
    }

    @Test
    void testEqualsAndHashCode() {
        UUID uuid = UUID.randomUUID();
        ProductId productId1 = new ProductId(uuid);
        ProductId productId2 = new ProductId(uuid);
        assertEquals(productId1, productId2);
        assertEquals(productId1.hashCode(), productId2.hashCode());
    }
}