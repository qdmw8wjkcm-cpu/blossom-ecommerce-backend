package org.blossom.ecommerce.Products.Application.Service;

import org.blossom.ecommerce.Products.Domain.Enums.ProductMessage;
import org.blossom.ecommerce.Products.Domain.Enums.StatusProduct;
import org.blossom.ecommerce.Products.Domain.Exceptions.DomainValidationException;
import org.blossom.ecommerce.Products.Domain.Models.Product;
import org.blossom.ecommerce.Products.Domain.Spec.AddComment;
import org.blossom.ecommerce.Products.Domain.Spec.ActivateDiscount;
import org.blossom.ecommerce.Products.Domain.Spec.CreateProductSpec;
import org.blossom.ecommerce.Products.Domain.Spec.RateProduct;
import org.blossom.ecommerce.Products.Domain.Spec.UpdateProduct;
import org.blossom.ecommerce.Products.Domain.ValueObjects.CategoryId;
import org.blossom.ecommerce.Products.Domain.ValueObjects.ProductId;
import org.blossom.ecommerce.Products.Infrastructure.Ports.Out.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class ProductServiceTest {

    @MockBean
    private ProductRepository repo;

    @Autowired
    private ProductService productService;

    private Product testProduct;
    private ProductId testProductId;
    private CategoryId testCategoryId;

    @BeforeEach
    void setUp() {
        testProductId = new ProductId(UUID.randomUUID());
        testCategoryId = new CategoryId(UUID.randomUUID());

        testProduct = Product.create("Test Product", "Description", BigDecimal.TEN, testCategoryId, 10, "Country", "City");
        testProduct = new Product(
                testProductId, testProduct.getTitle(), testProduct.getDescription(), testProduct.getPrice(),
                testProduct.getCategory(), testProduct.isDiscount(), testProduct.getDiscountPercentage(),
                testProduct.getDiscountAmount(), testProduct.getStock(), testProduct.getCountry(), testProduct.getCity(),
                testProduct.getCreatedAt(), testProduct.getUpdatedAt(), testProduct.getDiscountActiveAt(),
                testProduct.getDiscountInActiveAt(), testProduct.getRating(), testProduct.getComments(), testProduct.getStatus()
        );
    }

    @Test
    void testCreateProduct_Success() {
        CreateProductSpec spec = new CreateProductSpec(
                "New Product", "New Description", new BigDecimal("20.00"), testCategoryId, 5,
                "New Country", "New City"
        );
        when(repo.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Product> createdProduct = productService.create(spec);

        assertTrue(createdProduct.isPresent());
        assertEquals("New Product", createdProduct.get().getTitle());
        verify(repo, times(1)).save(any(Product.class));
    }

    @Test
    void testCreateProduct_EmptyTitle_ShouldThrowException() {
        CreateProductSpec spec = new CreateProductSpec(
                "", "New Description", new BigDecimal("20.00"), testCategoryId, 5,
                "New Country", "New City"
        );
        DomainValidationException exception = assertThrows(DomainValidationException.class, () -> {
            productService.create(spec);
        });
        assertEquals(ProductMessage.EMPTY_TITLE.getMessage(), exception.getMessage());
        verify(repo, never()).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_Success() {
        UpdateProduct cmd = new UpdateProduct(testProductId, "Updated Title", null, null, null, null, null);
        when(repo.findById(testProductId)).thenReturn(Optional.of(testProduct));
        when(repo.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product updatedProduct = productService.update(cmd);

        assertNotNull(updatedProduct);
        assertEquals("Updated Title", updatedProduct.getTitle());
        verify(repo, times(1)).findById(testProductId);
        verify(repo, times(1)).save(any(Product.class));
    }

    @Test
    void testDeleteProduct_Success() {
        when(repo.findById(testProductId)).thenReturn(Optional.of(testProduct));
        when(repo.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        productService.delete(testProductId);

        assertEquals(StatusProduct.DELETE, testProduct.getStatus());
        verify(repo, times(1)).findById(testProductId);
        verify(repo, times(1)).save(any(Product.class));
    }

    @Test
    void testActivateDiscount_Success() {
        ActivateDiscount cmd = new ActivateDiscount(testProductId, 10L, null, Instant.now(), Instant.now().plusSeconds(3600));
        when(repo.findById(testProductId)).thenReturn(Optional.of(testProduct));
        when(repo.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product discountedProduct = productService.activateDiscount(cmd);

        assertTrue(discountedProduct.isDiscount());
        assertEquals(10L, discountedProduct.getDiscountPercentage());
        verify(repo, times(1)).findById(testProductId);
        verify(repo, times(1)).save(any(Product.class));
    }

    @Test
    void testRateProduct_Success() {
        RateProduct cmd = new RateProduct(testProductId, 4.5);
        when(repo.findById(testProductId)).thenReturn(Optional.of(testProduct));
        when(repo.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product ratedProduct = productService.rate(cmd);

        assertEquals(4.5, ratedProduct.getRating());
        verify(repo, times(1)).findById(testProductId);
        verify(repo, times(1)).save(any(Product.class));
    }

    @Test
    void testAddComment_Success() {
        AddComment cmd = new AddComment(testProductId, "Great product!");
        when(repo.findById(testProductId)).thenReturn(Optional.of(testProduct));
        when(repo.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product commentedProduct = productService.addComment(cmd);

        assertTrue(commentedProduct.getComments().contains("Great product!"));
        verify(repo, times(1)).findById(testProductId);
        verify(repo, times(1)).save(any(Product.class));
    }

    @Test
    void testFindById_Found() {
        when(repo.findById(testProductId)).thenReturn(Optional.of(testProduct));

        Optional<Product> foundProduct = productService.findById(testProductId);

        assertTrue(foundProduct.isPresent());
        assertEquals(testProductId, foundProduct.get().getId());
        verify(repo, times(1)).findById(testProductId);
    }

    @Test
    void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        when(repo.findAll(pageable)).thenReturn(new PageImpl<>(List.of(testProduct), pageable, 1));

        var productsPage = productService.findAll(pageable);

        assertEquals(1, productsPage.getTotalElements());
        assertEquals(testProductId, productsPage.getContent().get(0).getId());
        verify(repo, times(1)).findAll(pageable);
    }
}
