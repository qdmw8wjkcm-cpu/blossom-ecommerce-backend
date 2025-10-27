package org.blossom.ecommerce.Products.Adapters.Jpa;

import org.blossom.ecommerce.Products.Adapters.Mappers.ProductMapper;
import org.blossom.ecommerce.Products.Domain.Enums.StatusProduct;
import org.blossom.ecommerce.Products.Domain.Models.Product;
import org.blossom.ecommerce.Products.Domain.ValueObjects.CategoryId;
import org.blossom.ecommerce.Products.Domain.ValueObjects.ProductId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

// Mock ProductEntity for testing purposes, as its definition is not available in this context
class ProductEntity {
    private UUID id;
    private String title;
    private String description;
    private BigDecimal price;
    private UUID categoryId;
    private boolean discount;
    private Long discountPercentage;
    private BigDecimal discountAmount;
    private int stock;
    private String country;
    private String city;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant discountActiveAt;
    private Instant discountInActiveAt;
    private Double rating;
    private List<String> comments;
    private StatusProduct status;

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public UUID getCategoryId() { return categoryId; }
    public void setCategoryId(UUID categoryId) { this.categoryId = categoryId; }
    public boolean isDiscount() { return discount; }
    public void setDiscount(boolean discount) { this.discount = discount; }
    public Long getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(Long discountPercentage) { this.discountPercentage = discountPercentage; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public Instant getDiscountActiveAt() { return discountActiveAt; }
    public void setDiscountActiveAt(Instant discountActiveAt) { this.discountActiveAt = discountActiveAt; }
    public Instant getDiscountInActiveAt() { return discountInActiveAt; }
    public void setDiscountInActiveAt(Instant discountInActiveAt) { this.discountInActiveAt = discountInActiveAt; }
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
    public List<String> getComments() { return comments; }
    public void setComments(List<String> comments) { this.comments = comments; }
    public StatusProduct getStatus() { return status; }
    public void setStatus(StatusProduct status) { this.status = status; }
}

@DataJpaTest
@Import({JpaProductRepositoryAdapter.class, ProductMapper.class}) // Import the adapter and mapper
@ActiveProfiles("test")
class JpaProductRepositoryAdapterTest {

    @Autowired
    private JpaProductRepositoryAdapter adapter;

    @Autowired
    private ProductJpaRepository jpaRepository; // To verify direct DB state if needed

    private Product product1;
    private Product product2;
    private CategoryId categoryId1;
    private CategoryId categoryId2;

    @BeforeEach
    void setUp() {
        // Clear database before each test
        jpaRepository.deleteAll();

        categoryId1 = new CategoryId(UUID.randomUUID());
        categoryId2 = new CategoryId(UUID.randomUUID());

        product1 = Product.create(
                "Laptop", "Powerful laptop", new BigDecimal("1200.00"), categoryId1, 10, "USA", "NY");
        // Manually set ID for testing consistency, as create() generates null ID
        product1 = new Product(
                new ProductId(UUID.randomUUID()), product1.getTitle(), product1.getDescription(),
                product1.getPrice(), product1.getCategory(), product1.isDiscount(), product1.getDiscountPercentage(),
                product1.getDiscountAmount(), product1.getStock(), product1.getCountry(), product1.getCity(),
                product1.getCreatedAt(), product1.getUpdatedAt(), product1.getDiscountActiveAt(),
                product1.getDiscountInActiveAt(), product1.getRating(), product1.getComments(), product1.getStatus()
        );

        product2 = Product.create(
                "Mouse", "Wireless mouse", new BigDecimal("25.00"), categoryId2, 50, "Germany", "Berlin");
        product2 = new Product(
                new ProductId(UUID.randomUUID()), product2.getTitle(), product2.getDescription(),
                product2.getPrice(), product2.getCategory(), product2.isDiscount(), product2.getDiscountPercentage(),
                product2.getDiscountAmount(), product2.getStock(), product2.getCountry(), product2.getCity(),
                product2.getCreatedAt(), product2.getUpdatedAt(), product2.getDiscountActiveAt(),
                product2.getDiscountInActiveAt(), product2.getRating(), product2.getComments(), product2.getStatus()
        );
    }

    @Test
    void testSaveProduct() {
        Product savedProduct = adapter.save(product1);

        assertNotNull(savedProduct.getId());
        assertEquals(product1.getTitle(), savedProduct.getTitle());
        assertEquals(product1.getPrice(), savedProduct.getPrice());

        assertTrue(jpaRepository.findById(savedProduct.getId().value()).isPresent());
    }

    @Test
    void testFindById() {
        adapter.save(product1);

        Optional<Product> foundProduct = adapter.findById(product1.getId());

        assertTrue(foundProduct.isPresent());
        assertEquals(product1.getId(), foundProduct.get().getId());
        assertEquals(product1.getTitle(), foundProduct.get().getTitle());
    }

    @Test
    void testFindAll() {
        adapter.save(product1);
        adapter.save(product2);

        PageRequest pageRequest = PageRequest.of(0, 10);
        var productsPage = adapter.findAll(pageRequest);

        assertEquals(2, productsPage.getTotalElements());
        assertEquals(1, productsPage.getTotalPages());
        assertTrue(productsPage.getContent().contains(product1)); // Assuming equals/hashCode are implemented in Product
    }

    @Test
    void testDeleteProduct() {
        adapter.save(product1);
        assertTrue(jpaRepository.findById(product1.getId().value()).isPresent());

        adapter.delete(product1.getId());

        assertFalse(jpaRepository.findById(product1.getId().value()).isPresent());
    }

    @Test
    void testFindByCategory() {
        adapter.save(product1);
        adapter.save(product2);

        PageRequest pageRequest = PageRequest.of(0, 10);
        var productsPage = adapter.findByCategory(categoryId1, pageRequest);

        assertEquals(1, productsPage.getTotalElements());
        assertEquals(product1.getId(), productsPage.getContent().get(0).getId());
    }

    @Test
    void testFindByPriceBetween() {
        adapter.save(product1); // 1200
        adapter.save(product2); // 25

        Product product3 = Product.create("Keyboard", "Gaming keyboard", new BigDecimal("150.00"), categoryId1, 20, "USA", "LA");
        product3 = new Product(
                new ProductId(UUID.randomUUID()), product3.getTitle(), product3.getDescription(),
                product3.getPrice(), product3.getCategory(), product3.isDiscount(), product3.getDiscountPercentage(),
                product3.getDiscountAmount(), product3.getStock(), product3.getCountry(), product3.getCity(),
                product3.getCreatedAt(), product3.getUpdatedAt(), product3.getDiscountActiveAt(),
                product3.getDiscountInActiveAt(), product3.getRating(), product3.getComments(), product3.getStatus()
        );
        adapter.save(product3);

        PageRequest pageRequest = PageRequest.of(0, 10);
        var productsPage = adapter.findByPriceBetween(new BigDecimal("100.00"), new BigDecimal("1000.00"), pageRequest);

        assertEquals(1, productsPage.getTotalElements());
        assertEquals(product3.getId(), productsPage.getContent().get(0).getId());
    }

    @Test
    void testFindByTitleContains() {
        adapter.save(product1);
        adapter.save(product2);

        PageRequest pageRequest = PageRequest.of(0, 10);
        var productsPage = adapter.findByTitleContains("lap", pageRequest);

        assertEquals(1, productsPage.getTotalElements());
        assertEquals(product1.getId(), productsPage.getContent().get(0).getId());
    }

    @Test
    void testFindDiscounted() {
        product1.activateDiscount(10L, null, Instant.now(), Instant.now().plusSeconds(3600));
        adapter.save(product1);
        adapter.save(product2);

        PageRequest pageRequest = PageRequest.of(0, 10);
        var productsPage = adapter.findDiscounted(pageRequest);

        assertEquals(1, productsPage.getTotalElements());
        assertEquals(product1.getId(), productsPage.getContent().get(0).getId());
    }

    @Test
    void testFindByCountry() {
        adapter.save(product1);
        adapter.save(product2);

        PageRequest pageRequest = PageRequest.of(0, 10);
        var productsPage = adapter.findByCountry("USA", pageRequest);

        assertEquals(1, productsPage.getTotalElements());
        assertEquals(product1.getId(), productsPage.getContent().get(0).getId());
    }

    @Test
    void testFindByCity() {
        adapter.save(product1);
        adapter.save(product2);

        PageRequest pageRequest = PageRequest.of(0, 10);
        var productsPage = adapter.findByCity("Berlin", pageRequest);

        assertEquals(1, productsPage.getTotalElements());
        assertEquals(product2.getId(), productsPage.getContent().get(0).getId());
    }
}
