package org.blossom.ecommerce.Products.Adapters.Jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {

    Page<ProductEntity> findByCategoryId(UUID categoryId, Pageable pageable);

    @Query("SELECT p FROM ProductEntity p WHERE p.price BETWEEN :min AND :max")
    Page<ProductEntity> findByPriceBetween(BigDecimal min, BigDecimal max, Pageable pageable);

    @Query("SELECT p FROM ProductEntity p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Page<ProductEntity> findByTitleContains(String title, Pageable pageable);

    @Query("SELECT p FROM ProductEntity p WHERE p.rating >= :rating")
    Page<ProductEntity> findByRatingAtLeast(Double rating, Pageable pageable);

    Page<ProductEntity> findByDiscountTrue(Pageable pageable);

    @Query("SELECT p FROM ProductEntity p WHERE LOWER(p.country) = LOWER(:country)")
    Page<ProductEntity> findByCountry(String country, Pageable pageable);

    @Query("SELECT p FROM ProductEntity p WHERE LOWER(p.city) = LOWER(:city)")
    Page<ProductEntity> findByCity(String city, Pageable pageable);
}