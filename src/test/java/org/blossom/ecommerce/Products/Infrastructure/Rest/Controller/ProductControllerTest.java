package org.blossom.ecommerce.Products.Infrastructure.Rest.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.blossom.ecommerce.Products.Application.Service.ProductService;
import org.blossom.ecommerce.Products.Domain.Enums.StatusProduct;
import org.blossom.ecommerce.Products.Domain.Models.Product;
import org.blossom.ecommerce.Products.Domain.ValueObjects.CategoryId;
import org.blossom.ecommerce.Products.Domain.ValueObjects.ProductId;
import org.blossom.ecommerce.Products.Infrastructure.Rest.Dto.ActivateDiscountRequest;
import org.blossom.ecommerce.Products.Infrastructure.Rest.Dto.AddCommentRequest;
import org.blossom.ecommerce.Products.Infrastructure.Rest.Dto.NewProduct;
import org.blossom.ecommerce.Products.Infrastructure.Rest.Dto.RateProductRequest;
import org.blossom.ecommerce.Products.Infrastructure.Rest.Dto.UpdateProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private ProductId testProductId;
    private CategoryId testCategoryId;
    private Product testProduct;

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
    void testCreateProduct_Success() throws Exception {
        NewProduct newProductDto = new NewProduct(
                "New Product", "New Desc", new BigDecimal("100.00"), testCategoryId.value(), 10, "USA", "NYC");

        when(productService.create(any())).thenReturn(Optional.of(testProduct));

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProductDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id.value", is(testProductId.value().toString())));

        verify(productService, times(1)).create(any());
    }

    @Test
    void testUpdateProduct_Success() throws Exception {
        UpdateProductRequest updateRequest = new UpdateProductRequest("Updated Title", null, null, null, null, null);

        when(productService.update(any())).thenReturn(testProduct);

        mockMvc.perform(put("/api/products/{id}", testProductId.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.value", is(testProductId.value().toString())));

        verify(productService, times(1)).update(any());
    }

    @Test
    void testDeleteProduct_Success() throws Exception {
        doNothing().when(productService).delete(any(ProductId.class));

        mockMvc.perform(delete("/api/products/{id}", testProductId.value()))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).delete(any(ProductId.class));
    }

    @Test
    void testActivateDiscount_Success() throws Exception {
        ActivateDiscountRequest discountRequest = new ActivateDiscountRequest(10L, null, Instant.now(), Instant.now().plusSeconds(3600));

        when(productService.activateDiscount(any())).thenReturn(testProduct);

        mockMvc.perform(post("/api/products/{id}/discount", testProductId.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(discountRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.value", is(testProductId.value().toString())));

        verify(productService, times(1)).activateDiscount(any());
    }

    @Test
    void testRateProduct_Success() throws Exception {
        RateProductRequest rateRequest = new RateProductRequest(4.5);

        when(productService.rate(any())).thenReturn(testProduct);

        mockMvc.perform(post("/api/products/{id}/rating", testProductId.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.value", is(testProductId.value().toString())));

        verify(productService, times(1)).rate(any());
    }

    @Test
    void testAddComment_Success() throws Exception {
        AddCommentRequest commentRequest = new AddCommentRequest("Great product!");

        when(productService.addComment(any())).thenReturn(testProduct);

        mockMvc.perform(post("/api/products/{id}/comments", testProductId.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.value", is(testProductId.value().toString())));

        verify(productService, times(1)).addComment(any());
    }

    @Test
    void testFindById_Found() throws Exception {
        when(productService.findById(testProductId)).thenReturn(Optional.of(testProduct));

        mockMvc.perform(get("/api/products/{id}", testProductId.value()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.value", is(testProductId.value().toString())));

        verify(productService, times(1)).findById(testProductId);
    }

    @Test
    void testFindById_NotFound() throws Exception {
        when(productService.findById(any(ProductId.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/products/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).findById(any(ProductId.class));
    }

    @Test
    void testListProducts_NoFilters() throws Exception {
        when(productService.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(testProduct), PageRequest.of(0, 20), 1));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id.value", is(testProductId.value().toString())));

        verify(productService, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testListProducts_ByCategory() throws Exception {
        when(productService.findByCategory(any(CategoryId.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(testProduct), PageRequest.of(0, 20), 1));

        mockMvc.perform(get("/api/products")
                        .param("categoryId", testCategoryId.value().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id.value", is(testProductId.value().toString())));

        verify(productService, times(1)).findByCategory(any(CategoryId.class), any(Pageable.class));
    }

    @Test
    void testListProducts_ByTitleContains() throws Exception {
        when(productService.findByTitleContains(anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(testProduct), PageRequest.of(0, 20), 1));

        mockMvc.perform(get("/api/products")
                        .param("title", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id.value", is(testProductId.value().toString())));

        verify(productService, times(1)).findByTitleContains(anyString(), any(Pageable.class));
    }

    @Test
    void testListProducts_ByDiscounted() throws Exception {
        when(productService.findDiscounted(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(testProduct), PageRequest.of(0, 20), 1));

        mockMvc.perform(get("/api/products")
                        .param("discounted", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id.value", is(testProductId.value().toString())));

        verify(productService, times(1)).findDiscounted(any(Pageable.class));
    }
}
