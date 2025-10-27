package org.blossom.ecommerce.Products.Domain.Models;

import org.blossom.ecommerce.Products.Domain.Enums.ProductMessage;
import org.blossom.ecommerce.Products.Domain.Enums.StatusProduct;
import org.blossom.ecommerce.Products.Domain.Exceptions.DomainValidationException;
import org.blossom.ecommerce.Products.Domain.ValueObjects.CategoryId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private CategoryId categoryId;

    @BeforeEach
    void setUp() {
        categoryId = new CategoryId(UUID.randomUUID());
    }

    @Test
    void testCreateProduct_Success() {
        Product product = Product.create("Test Product", "A great product", new BigDecimal("99.99"), categoryId, 100, "USA", "NYC");

        assertNotNull(product);
        assertEquals("Test Product", product.getTitle());
        assertEquals(new BigDecimal("99.99"), product.getPrice());
        assertEquals(100, product.getStock());
        assertEquals(StatusProduct.ACTIVE, product.getStatus());
        assertFalse(product.isDiscount());
        assertEquals(0.0, product.getRating());
        assertTrue(product.getComments().isEmpty());
    }

    @Test
    void testCreateProduct_InvalidPrice_ShouldThrowException() {
        assertThrows(DomainValidationException.class, () -> {
            Product.create("Test", "Desc", new BigDecimal("-1"), categoryId, 10, null, null);
        });
    }

    @Test
    void testCreateProduct_InvalidStock_ShouldThrowException() {
        assertThrows(DomainValidationException.class, () -> {
            Product.create("Test", "Desc", BigDecimal.TEN, categoryId, -1, null, null);
        });
    }

    @Test
    void testDecreaseStock_Success() {
        Product product = Product.create("Test", "Desc", BigDecimal.TEN, categoryId, 10, null, null);
        product.decreaseStock(3);
        assertEquals(7, product.getStock());
    }

    @Test
    void testDecreaseStock_InsufficientStock_ShouldThrowException() {
        Product product = Product.create("Test", "Desc", BigDecimal.TEN, categoryId, 2, null, null);
        DomainValidationException exception = assertThrows(DomainValidationException.class, () -> {
            product.decreaseStock(3);
        });
        assertEquals(ProductMessage.STOCK_INSUFFICIENT.getMessage(), exception.getMessage());
    }

    @Test
    void testActivateAndClearDiscount_ByPercentage() {
        Product product = Product.create("Test", "Desc", BigDecimal.TEN, categoryId, 10, null, null);
        product.activateDiscount(15L, null, null, null);

        assertTrue(product.isDiscount());
        assertEquals(15L, product.getDiscountPercentage());
        assertNull(product.getDiscountAmount());

        product.clearDiscount();
        assertFalse(product.isDiscount());
    }

    @Test
    void testActivateDiscount_InvalidPercentage_ShouldThrowException() {
        Product product = Product.create("Test", "Desc", BigDecimal.TEN, categoryId, 10, null, null);
        assertThrows(DomainValidationException.class, () -> {
            product.activateDiscount(101L, null, null, null);
        });
    }

    @Test
    void testSetRating_Success() {
        Product product = Product.create("Test", "Desc", BigDecimal.TEN, categoryId, 10, null, null);
        product.setRating(4.5);
        assertEquals(4.5, product.getRating());
    }

    @Test
    void testSetRating_InvalidRating_ShouldThrowException() {
        Product product = Product.create("Test", "Desc", BigDecimal.TEN, categoryId, 10, null, null);
        assertThrows(DomainValidationException.class, () -> {
            product.setRating(6.0);
        });
    }

    @Test
    void testAddComment_Success() {
        Product product = Product.create("Test", "Desc", BigDecimal.TEN, categoryId, 10, null, null);
        product.addComment("This is a great comment!");
        assertEquals(1, product.getComments().size());
        assertEquals("This is a great comment!", product.getComments().get(0));
    }

    @Test
    void testDeleteAndActivateProduct() {
        Product product = Product.create("Test", "Desc", BigDecimal.TEN, categoryId, 10, null, null);
        product.delete();
        assertEquals(StatusProduct.DELETE, product.getStatus());
        assertTrue(product.isDeleted());

        product.activate();
        assertEquals(StatusProduct.ACTIVE, product.getStatus());
        assertFalse(product.isDeleted());
    }
}