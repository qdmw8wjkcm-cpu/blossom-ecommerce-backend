package org.blossom.ecommerce.Users.Domain.ValueObjects;

import org.blossom.ecommerce.Users.Domain.Enums.ErrorMessage;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CategoryIdTest {

    @Test
    void testCreateCategoryId_Success() {
        UUID uuid = UUID.randomUUID();
        CategoryId categoryId = new CategoryId(uuid);
        assertNotNull(categoryId);
        assertEquals(uuid, categoryId.value());
    }

    @Test
    void testCreateCategoryId_NullValue_ShouldThrowException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            new CategoryId(null);
        });
        assertEquals(ErrorMessage.CATEGORY_NULL.getMessage(), exception.getMessage());
    }

    @Test
    void testCreateCategoryId_ZeroUUID_ShouldThrowException() {
        UUID zeroUUID = new UUID(0L, 0L);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new CategoryId(zeroUUID);
        });
        assertEquals(ErrorMessage.CATEGORY_ZERO.getMessage(), exception.getMessage());
    }

    @Test
    void testCategoryIdStaticMethod_ShouldReturnNonNull() {
        CategoryId categoryId = CategoryId.categoryId();
        assertNotNull(categoryId);
        assertNotNull(categoryId.value());
    }

    @Test
    void testFromString_ValidString_Success() {
        String uuidString = UUID.randomUUID().toString();
        CategoryId categoryId = CategoryId.fromString(uuidString);
        assertNotNull(categoryId);
        assertEquals(UUID.fromString(uuidString), categoryId.value());
    }

    @Test
    void testFromString_InvalidString_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            CategoryId.fromString("invalid-uuid");
        });
    }

    @Test
    void testEqualsAndHashCode() {
        UUID uuid = UUID.randomUUID();
        CategoryId categoryId1 = new CategoryId(uuid);
        CategoryId categoryId2 = new CategoryId(uuid);
        assertEquals(categoryId1, categoryId2);
        assertEquals(categoryId1.hashCode(), categoryId2.hashCode());
    }
}