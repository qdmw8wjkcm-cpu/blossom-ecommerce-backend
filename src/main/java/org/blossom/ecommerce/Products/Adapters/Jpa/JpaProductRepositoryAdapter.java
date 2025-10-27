package org.blossom.ecommerce.Products.Adapters.Jpa;

import lombok.RequiredArgsConstructor;
import org.blossom.ecommerce.Products.Adapters.Mappers.ProductMapper;
import org.blossom.ecommerce.Products.Domain.Models.Product;
import org.blossom.ecommerce.Products.Domain.ValueObjects.CategoryId;
import org.blossom.ecommerce.Products.Domain.ValueObjects.ProductId;
import org.blossom.ecommerce.Products.Infrastructure.Ports.Out.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaProductRepositoryAdapter implements ProductRepository {

    private final ProductJpaRepository jpaRepo;

    @Override
    public Product save(Product product) {
        var entity = ProductMapper.toEntity(product);
        var saved = jpaRepo.save(entity);
        return ProductMapper.toDomain(saved);
    }

    @Override
    public void delete(ProductId id) {
        jpaRepo.deleteById(id.value());
    }

    @Override
    public Optional<Product> findById(ProductId id) {
        return jpaRepo.findById(id.value()).map(ProductMapper::toDomain);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return jpaRepo.findAll(pageable).map(ProductMapper::toDomain);
    }

    @Override
    public Page<Product> findByCategory(CategoryId categoryId, Pageable pageable) {
        return jpaRepo.findByCategoryId(categoryId.value(), pageable).map(ProductMapper::toDomain);
    }

    @Override
    public Page<Product> findByPriceBetween(BigDecimal min, BigDecimal max, Pageable pageable) {
        return jpaRepo.findByPriceBetween(min, max, pageable).map(ProductMapper::toDomain);
    }

    @Override
    public Page<Product> findByTitleContains(String title, Pageable pageable) {
        return jpaRepo.findByTitleContains(title, pageable).map(ProductMapper::toDomain);
    }

    @Override
    public Page<Product> findByRatingAtLeast(Double rating, Pageable pageable) {
        return jpaRepo.findByRatingAtLeast(rating, pageable).map(ProductMapper::toDomain);
    }

    @Override
    public Page<Product> findDiscounted(Pageable pageable) {
        return jpaRepo.findByDiscountTrue(pageable).map(ProductMapper::toDomain);
    }

    @Override
    public Page<Product> findByCountry(String country, Pageable pageable) {
        return jpaRepo.findByCountry(country, pageable).map(ProductMapper::toDomain);
    }

    @Override
    public Page<Product> findByCity(String city, Pageable pageable) {
        return jpaRepo.findByCity(city, pageable).map(ProductMapper::toDomain);
    }
}