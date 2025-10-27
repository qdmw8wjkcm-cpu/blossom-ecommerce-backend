package org.blossom.ecommerce.Orders.Adapters.Jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.blossom.ecommerce.Orders.Infrastructure.Ports.Out.OrderRepository;
import org.blossom.ecommerce.Orders.Adapters.Mappers.OrderMapper;
import org.blossom.ecommerce.Orders.Domain.Models.Order;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.OrderId;
import org.blossom.ecommerce.Orders.Domain.ValueObjects.UserId;
import org.blossom.ecommerce.Orders.Domain.Enums.OrderStatus;

import java.time.Instant;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaOrderRepositoryAdapter implements OrderRepository {

    private final OrderJpaRepository jpaRepo;

    @Override
    public Order save(Order order) {
        var entity = OrderMapper.toEntity(order);
        var saved = jpaRepo.save(entity);
        return OrderMapper.toDomain(saved);
    }

    @Override
    public Optional<Order> findById(OrderId id) {
        return jpaRepo.findById(id.value())
                .map(OrderMapper::toDomain);
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return jpaRepo.findAll(pageable).map(OrderMapper::toDomain);
    }

    @Override
    public Page<Order> findByUser(UserId userId, Pageable pageable) {
        return jpaRepo.findByUserId(userId.value(), pageable).map(OrderMapper::toDomain);
    }

    @Override
    public Page<Order> findByStatus(OrderStatus status, Pageable pageable) {
        return jpaRepo.findByStatus(status.name(), pageable).map(OrderMapper::toDomain);
    }


    @Override
    public Page<Order> findByDateRange(Instant start, Instant end, Pageable pageable) {
        return jpaRepo.findByCreatedAtBetween(start, end, pageable).map(OrderMapper::toDomain);
    }
}