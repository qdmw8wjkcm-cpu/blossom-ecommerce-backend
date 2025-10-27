package org.blossom.ecommerce.Products.Domain.Models;

import org.blossom.ecommerce.Products.Domain.Enums.ProductMessage;
import org.blossom.ecommerce.Products.Domain.Enums.StatusProduct;
import org.blossom.ecommerce.Products.Domain.Exceptions.DomainValidationException;
import org.blossom.ecommerce.Products.Domain.Spec.CreateProductSpec;
import org.blossom.ecommerce.Products.Domain.Spec.UpdateProduct;
import org.blossom.ecommerce.Products.Domain.ValueObjects.CategoryId;
import org.blossom.ecommerce.Products.Domain.ValueObjects.ProductId;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Product {

    private ProductId id;
    private String title;
    private String description;
    private BigDecimal price;
    private CategoryId category;
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

    public Product(){}

    public Product(
            ProductId id,
            String title,
            String description,
            BigDecimal price,
            CategoryId category,
            boolean discount,
            Long discountPercentage,
            BigDecimal discountAmount,
            int stock,
            String country,
            String city,
            Instant createdAt,
            Instant updatedAt,
            Instant discountActiveAt,
            Instant discountInActiveAt,
            Double rating,
            List<String> comments,
            StatusProduct status
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.discount = discount;
        this.discountPercentage = discountPercentage;
        this.discountAmount = discountAmount;
        this.stock = stock;
        this.country = country;
        this.city = city;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.discountActiveAt = discountActiveAt;
        this.discountInActiveAt = discountInActiveAt;
        this.rating = rating;
        this.comments = comments != null ? new ArrayList<>(comments) : new ArrayList<>();
        this.status = status;
    }

    public static Product create(
            String title,
            String description,
            BigDecimal price,
            CategoryId category,
            int stock,
            String country,
            String city
    ) {
        if (title == null || title.isBlank())
            throw new DomainValidationException(ProductMessage.EMPTY_TITLE.getMessage());

        if (description == null || description.isBlank())
            throw new DomainValidationException(ProductMessage.EMPTY_DESCRIPTION.getMessage());

        if (category == null)
            throw new DomainValidationException(ProductMessage.EMPTY_CATEGORY.getMessage());

        if (price == null || price.signum() < 0)
            throw new DomainValidationException(ProductMessage.INVALID_PRICE.getMessage());

        if (stock < 0)
            throw new DomainValidationException(ProductMessage.INVALID_STOCK.getMessage());

        Instant now = Instant.now();

        Product p = new Product();
        p.id = null;
        p.title = title;
        p.description = description;
        p.price = price;
        p.category = category;
        p.discount = false;
        p.discountPercentage = null;
        p.discountAmount = null;
        p.stock = stock;
        p.country = country;
        p.city = city;
        p.createdAt = now;
        p.updatedAt = now;
        p.discountActiveAt = null;
        p.discountInActiveAt = null;
        p.rating = 0.0;
        p.comments = new ArrayList<>();
        p.status = StatusProduct.ACTIVE;
        return p;
    }

    public void changePrice(BigDecimal newPrice) {
        if (newPrice == null || newPrice.signum() < 0)
            throw new DomainValidationException(ProductMessage.INVALID_PRICE.getMessage());
        this.price = newPrice;
        touch();
    }

    public void setStock(int newStock) {
        if (newStock < 0)
            throw new DomainValidationException(ProductMessage.INVALID_STOCK.getMessage());
        this.stock = newStock;
        touch();
    }

    public void delete() {
        this.status = StatusProduct.DELETE;
        touch();
    }

    public void activate() {
        this.status = StatusProduct.ACTIVE;
        this.updatedAt = Instant.now();
    }

    public void deactivate() {
        this.status = StatusProduct.INACTIVE;
        this.updatedAt = Instant.now();
    }

    public void decreaseStock(int amount) {
        if (amount < 0)
            throw new DomainValidationException(ProductMessage.INVALID_STOCK.getMessage());
        if (this.stock - amount < 0)
            throw new DomainValidationException(ProductMessage.STOCK_INSUFFICIENT.getMessage());
        this.stock -= amount;
        touch();
    }

    public void activateDiscount(Long percentage, BigDecimal amount, Instant since, Instant until) {
        if ((percentage == null && amount == null) || (percentage != null && amount != null))
            throw new DomainValidationException(ProductMessage.INVALID_DISCOUNT_AMOUNT.getMessage());

        if (percentage != null && (percentage < 1 || percentage > 100))
            throw new DomainValidationException(ProductMessage.INVALID_DISCOUNT_PERCENTAGE.getMessage());

        if (amount != null && amount.signum() <= 0)
            throw new DomainValidationException(ProductMessage.INVALID_DISCOUNT_AMOUNT.getMessage());

        Instant start = (since != null) ? since : Instant.now();
        if (until != null && !until.isAfter(start))
            throw new DomainValidationException(ProductMessage.INVALID_DISCOUNT_DATES.getMessage());

        this.discount = true;
        this.discountPercentage = percentage;
        this.discountAmount = amount;
        this.discountActiveAt = start;
        this.discountInActiveAt = until;
        touch();
    }
    public void clearDiscount() {
        this.discount = false;
        this.discountPercentage = null;
        this.discountAmount = null;
        this.discountActiveAt = null;
        this.discountInActiveAt = null;
        touch();
    }

    public void setRating(Double rating) {
        if (rating == null) {
            this.rating = null;
        } else if (rating < 0.0 || rating > 5.0) {
            throw new DomainValidationException(ProductMessage.INVALID_RATING.getMessage());
        } else {
            this.rating = rating;
        }
        touch();
    }

    public void addComment(String comment) {
        if (comment == null || comment.isBlank())
            throw new DomainValidationException(ProductMessage.EMPTY_COMMENT.getMessage());
        this.comments.add(comment);
        touch();
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    public void updateFrom(UpdateProduct cmd) {
        boolean changed = false;
        if (cmd.title() != null && !cmd.title().isBlank()) {
            this.title = cmd.title().trim();
            changed = true;
        }
        if (cmd.description() != null && !cmd.description().isBlank()) {
            this.description = cmd.description().trim();
            changed = true;
        }
        if (cmd.price() != null && cmd.price().compareTo(BigDecimal.ZERO) >= 0) {
            this.price = cmd.price();
            changed = true;
        }
        if (cmd.stock() != null && cmd.stock() >= 0) {
            this.stock = cmd.stock();
            changed = true;
        }
        if (cmd.country() != null && !cmd.country().isBlank()) {
            this.country = cmd.country().trim();
            changed = true;
        }
        if (cmd.city() != null && !cmd.city().isBlank()) {
            this.city = cmd.city().trim();
            changed = true;
        }
        if (changed) touch();
    }

    public StatusProduct getStatus() { return status; }
    public boolean isDeleted() { return this.status == StatusProduct.DELETE; }
    public ProductId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public CategoryId getCategory() {
        return category;
    }

    public boolean isDiscount() {
        return discount;
    }

    public Long getDiscountPercentage() {
        return discountPercentage;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public int getStock() {
        return stock;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDiscountActiveAt() {
        return discountActiveAt;
    }

    public Instant getDiscountInActiveAt() {
        return discountInActiveAt;
    }

    public Double getRating() {
        return rating;
    }

    public List<String> getComments() {
        return List.copyOf(comments);
    }
}