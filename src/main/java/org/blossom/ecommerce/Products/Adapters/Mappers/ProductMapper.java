package org.blossom.ecommerce.Products.Adapters.Mappers;

import org.blossom.ecommerce.Products.Adapters.Jpa.ProductEntity;
import org.blossom.ecommerce.Products.Domain.Models.Product;
import org.blossom.ecommerce.Products.Domain.ValueObjects.CategoryId;
import org.blossom.ecommerce.Products.Domain.ValueObjects.ProductId;

public class ProductMapper {

    public static Product toDomain(ProductEntity entity) {
        if (entity == null) return null;

        return new Product(
                new ProductId(entity.getId()),
                entity.getTitle(),
                entity.getDescription(),
                entity.getPrice(),
                new CategoryId(entity.getCategoryId()),
                entity.isDiscount(),
                entity.getDiscountPercentage(),
                entity.getDiscountAmount(),
                entity.getStock(),
                entity.getCountry(),
                entity.getCity(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDiscountActiveAt(),
                entity.getDiscountInActiveAt(),
                entity.getRating(),
                entity.getComments(),
                entity.getStatus());

    }

    public static ProductEntity toEntity(Product domain) {
        ProductEntity e = new ProductEntity();
        e.setId(domain.getId() != null ? domain.getId().value() : null);
        e.setTitle(domain.getTitle());
        e.setDescription(domain.getDescription());
        e.setPrice(domain.getPrice());
        e.setCategoryId(domain.getCategory().value());
        e.setDiscount(domain.isDiscount());
        e.setDiscountPercentage(domain.getDiscountPercentage());
        e.setDiscountAmount(domain.getDiscountAmount());
        e.setStock(domain.getStock());
        e.setCountry(domain.getCountry());
        e.setCity(domain.getCity());
        e.setCreatedAt(domain.getCreatedAt());
        e.setUpdatedAt(domain.getUpdatedAt());
        e.setDiscountActiveAt(domain.getDiscountActiveAt());
        e.setDiscountInActiveAt(domain.getDiscountInActiveAt());
        e.setRating(domain.getRating());
        e.setComments(domain.getComments());
        e.setStatus(domain.getStatus());
        return e;
    }
}