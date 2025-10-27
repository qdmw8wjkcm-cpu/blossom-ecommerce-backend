package org.blossom.ecommerce.Products.Domain.Ports.In;

import org.blossom.ecommerce.Products.Domain.Models.Product;
import org.blossom.ecommerce.Products.Domain.ValueObjects.ProductId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

public interface ProductQueryUseCase {
    Optional<Product> findById(ProductId id);

    Page<Product> findAll(Pageable pageable);
    Page<Product> findByPriceBetween(BigDecimal min, BigDecimal max, Pageable pageable);
    Page<Product> findByTitleContains(String title, Pageable pageable);
    Page<Product> findByRatingAtLeast(Double rating, Pageable pageable);
    Page<Product> findDiscounted(Pageable pageable);
    Page<Product> findByCountry(String country, Pageable pageable);
    Page<Product> findByCity(String city, Pageable pageable);
}