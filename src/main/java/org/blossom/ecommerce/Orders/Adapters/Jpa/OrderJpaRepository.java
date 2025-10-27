package org.blossom.ecommerce.Orders.Adapters.Jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, UUID> {

    Page<OrderEntity> findByUserId(UUID userId, Pageable pageable);

    Page<OrderEntity> findByStatus(String status, Pageable pageable);

    Page<OrderEntity> findByCreatedAtBetween(Instant start, Instant end, Pageable pageable);
}