package org.blossom.ecommerce.Users.Adapters.Persistence;

import org.blossom.ecommerce.Users.Adapters.Jpa.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByDocument(String document);

    Page<UserEntity> findByStatus(String status, Pageable pageable);
    Page<UserEntity> findByRole(String role, Pageable pageable);
    Page<UserEntity> findAllByStatus(String role, Pageable pageable);
    Page<UserEntity> findAllByRole(String role, Pageable pageable);
}