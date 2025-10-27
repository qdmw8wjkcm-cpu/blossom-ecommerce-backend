package org.blossom.ecommerce.Products.Infrastructure.Ports.Out;

import org.blossom.ecommerce.Products.Domain.Models.Product;

import org.blossom.ecommerce.Products.Domain.ValueObjects.CategoryId;
import org.blossom.ecommerce.Products.Domain.ValueObjects.ProductId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ProductRepository {

    Product save(Product product);
    void delete(ProductId id);

    Optional<Product> findById(ProductId id);

    Page<Product> findAll(Pageable pageable);
    Page<Product> findByCategory(CategoryId categoryId, Pageable pageable);
    Page<Product> findByPriceBetween(BigDecimal min, BigDecimal max, Pageable pageable);
    Page<Product> findByTitleContains(String title, Pageable pageable);
    Page<Product> findByRatingAtLeast(Double rating, Pageable pageable);
    Page<Product> findDiscounted(Pageable pageable);
    Page<Product> findByCountry(String country, Pageable pageable);
    Page<Product> findByCity(String city, Pageable pageable);
}