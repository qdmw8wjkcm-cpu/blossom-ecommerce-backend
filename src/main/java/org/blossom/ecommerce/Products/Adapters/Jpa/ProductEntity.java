package org.blossom.ecommerce.Products.Adapters.Jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.blossom.ecommerce.Products.Domain.Enums.StatusProduct;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
public class ProductEntity {

    @Id
    @GeneratedValue
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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_comments", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "comment")
    private List<String> comments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private StatusProduct status;
}