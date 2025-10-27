package org.blossom.ecommerce.Users.Adapters.Jpa;

import lombok.RequiredArgsConstructor;
import org.blossom.ecommerce.Users.Domain.Enums.Role;
import org.blossom.ecommerce.Users.Adapters.Mappers.UserMapper;
import org.blossom.ecommerce.Users.Adapters.Persistence.UserJpaRepository;
import org.blossom.ecommerce.Users.Domain.Enums.Status;
import org.blossom.ecommerce.Users.Domain.Models.User;
import org.blossom.ecommerce.Users.Domain.Ports.Out.UserRepository;
import org.blossom.ecommerce.Users.Domain.ValueObjects.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaUserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository springDataRepository;

    @Override
    public User save(User user) {
        UserEntity entity = UserMapper.toEntity(user);
        UserEntity saved = springDataRepository.save(entity);
        return UserMapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(UserId id) {
        return springDataRepository.findById(id.value())
                .map(UserMapper::toDomain);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByEmail(Email email) {
        return springDataRepository.findByEmail(email.value())
                .map(UserMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByDocument(DocumentId document) {
        return springDataRepository.findByDocument(document.document())
                .map(UserMapper::toDomain);
    }

    @Override
    public Page<User> findAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return springDataRepository.findAll(pageRequest)
                .map(UserMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAllByStatus(Status status, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return springDataRepository.findAllByStatus(status.name(), pageRequest)
                .map(UserMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAllByRole(Role role, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return springDataRepository.findAllByRole(role.name(), pageRequest)
                .map(UserMapper::toDomain);
    }
}