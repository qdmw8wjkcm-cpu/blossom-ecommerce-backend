package org.blossom.ecommerce.Users.Application.Service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.blossom.ecommerce.Users.Domain.Enums.Role;
import org.blossom.ecommerce.Users.Application.Dto.CreateUserSpec;
import org.blossom.ecommerce.Users.Application.Dto.UpdateUser;
import org.blossom.ecommerce.Users.Domain.Enums.ErrorMessage;
import org.blossom.ecommerce.Users.Domain.Enums.Status;
import org.blossom.ecommerce.Users.Domain.Exceptions.DomainValidationException;
import org.blossom.ecommerce.Users.Domain.Models.User;
import org.blossom.ecommerce.Users.Domain.Ports.In.UseCase.UserCommandUseCase;
import org.blossom.ecommerce.Users.Domain.Ports.In.UseCase.UserQueryUseCase;
import org.blossom.ecommerce.Users.Domain.Ports.Out.UserRepository;
import org.blossom.ecommerce.Users.Domain.ValueObjects.*;

import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserCommandUseCase, UserQueryUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> createProfile(CreateUserSpec newUser) {
        if (userRepository.findByEmail(newUser.email()).isPresent()) {
            log.error("User already exists with email {}", newUser.email());
            throw new DomainValidationException(ErrorMessage.USER_EXIST.getMessage());
        }

        if (userRepository.findByDocument(newUser.document()).isPresent()) {
            log.error("User already exists with document {}", newUser.document());
            throw new DomainValidationException(ErrorMessage.USER_EXIST.getMessage());
        }
        String bcrypt = passwordEncoder.encode(newUser.password());
        HashedPassword hashedPassword = HashedPassword.fromRaw(bcrypt);

        User user = User.create(
                newUser.name(),
                newUser.lastname(),
                hashedPassword,
                newUser.email(),
                newUser.document(),
                newUser.phoneNumber(),
                newUser.city(),
                newUser.country(),
                newUser.favoriteCategories(),
                newUser.favoriteProducts(),
                Role.ROLE_CUSTOMER
        );
        User saved = userRepository.save(user);
        return Optional.of(saved);
    }

    @Override
    public void changeEmail(UserId id, Email email) {
        User user = userRepository.findById(id).orElseThrow(() -> new DomainValidationException(ErrorMessage.USER_NOT_FOUND.getMessage()));

        if(user.getEmail().equals(email)) {
            throw new DomainValidationException(ErrorMessage.EMAIL_IS_THE_SAME.getMessage());
        }

        user.changeEmail(email);
        userRepository.save(user);
    }

    @Override
    public void changePassword(UserId id, HashedPassword password) {
        User user = userRepository.findById(id).orElseThrow(() -> new DomainValidationException(ErrorMessage.USER_NOT_FOUND.getMessage()));

        if(user.getPassword().equals(password)) {
            throw new DomainValidationException(ErrorMessage.EMAIL_IS_THE_SAME.getMessage());
        }

        user.changePassword(password);
        userRepository.save(user);
    }

    @Override
    public void changePhone(UserId id, PhoneNumber phone) {
        User user = userRepository.findById(id).orElseThrow(() -> new DomainValidationException(ErrorMessage.USER_NOT_FOUND.getMessage()));

        if(user.getPhoneNumber().equals(phone)) {
            throw new DomainValidationException(ErrorMessage.EMAIL_IS_THE_SAME.getMessage());
        }

        user.changePhoneNumber(phone);
        userRepository.save(user);
    }

    @Override
    public User updateProfile(UpdateUser cmd) {
        User existing = userRepository.findById(cmd.userId())
                .orElseThrow(() -> new DomainValidationException(ErrorMessage.USER_NOT_FOUND.getMessage()));

        if (cmd.email() != null && !cmd.email().equals(existing.getEmail())) {
            userRepository.findByEmail(cmd.email())
                    .filter(u -> !u.getId().equals(existing.getId()))
                    .ifPresent(u -> { throw new DomainValidationException(ErrorMessage.EMAIL_IN_USE.getMessage()); });
            existing.changeEmail(cmd.email());
        }

        if (cmd.password() != null) {
            existing.changePassword(cmd.password());
        }

        if (cmd.city() != null && !cmd.city().isBlank()) {
            existing.changeCity(cmd.city());
        }
        if (cmd.country() != null && !cmd.country().isBlank()) {
            existing.changeCountry(cmd.country());
        }
        if (cmd.favoriteCategoriesList() != null) {
            existing.getFavoriteCategories().forEach(id -> existing.removeFavoriteCategory(id));
            cmd.favoriteCategoriesList().forEach(existing::addFavoriteCategory);
        }
        if (cmd.favoriteProductsList() != null) {
            existing.getFavoriteProducts().forEach(id -> existing.removeFavoriteProduct(id));
            cmd.favoriteProductsList().forEach(existing::addFavoriteProduct);
        }
        User save = userRepository.save(existing);
        return save;
    }

    @Override
    public void changeCity(UserId id, String city) {
        User user = userRepository.findById(id).orElseThrow(() -> new DomainValidationException(ErrorMessage.USER_NOT_FOUND.getMessage()));

        if(user.getCity().equals(city)) {
            throw new DomainValidationException(ErrorMessage.EMAIL_IS_THE_SAME.getMessage());
        }

        user.changeCity(city);
        userRepository.save(user);
    }

    @Override
    public void changeCountry(UserId id, String country) {
        User user = userRepository.findById(id).orElseThrow(() -> new DomainValidationException(ErrorMessage.USER_NOT_FOUND.getMessage()));

        if(user.getCountry().equals(country)) {
            throw new DomainValidationException(ErrorMessage.EMAIL_IS_THE_SAME.getMessage());
        }

        user.changeCountry(country);
        userRepository.save(user);
    }

    @Override
    public void addFavoriteProduct(UserId userId, ProductId productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DomainValidationException(ErrorMessage.USER_NOT_FOUND.getMessage()));

        if(user.getFavoriteProducts().contains(productId)) {
            throw new DomainValidationException(ErrorMessage.PRODUCT_EXIST.getMessage());
        }

        user.addFavoriteProduct(productId);
        userRepository.save(user);
    }

    @Override
    public void removeFavoriteProduct(UserId userId, ProductId productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DomainValidationException(ErrorMessage.USER_NOT_FOUND.getMessage()));

        if(!user.getFavoriteProducts().contains(productId)) {
            throw new DomainValidationException(ErrorMessage.PRODUCT_NOT_EXIST.getMessage());
        }

        user.removeFavoriteProduct(productId);
        userRepository.save(user);
    }

    @Override
    public void addFavoriteCategory(UserId userId, CategoryId categoryId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DomainValidationException(ErrorMessage.USER_NOT_FOUND.getMessage()));

        if(user.getFavoriteCategories().contains(categoryId)) {
            throw new DomainValidationException(ErrorMessage.CATEGORY_EXIST.getMessage());
        }

        user.addFavoriteCategory(categoryId);
        userRepository.save(user);
    }

    @Override
    public void removeFavoriteCategory(UserId userId, CategoryId categoryId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DomainValidationException(ErrorMessage.USER_NOT_FOUND.getMessage()));

        if(!user.getFavoriteCategories().contains(categoryId)) {
            throw new DomainValidationException(ErrorMessage.CATEGORY_NOT_EXIST.getMessage());
        }

        user.removeFavoriteCategory(categoryId);
        userRepository.save(user);
    }


    @Override
    public void changeStatus(UserId id, Status newStatus) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DomainValidationException(ErrorMessage.USER_NOT_FOUND.getMessage()));

        if (user.getStatus().equals(newStatus)) {
            throw new DomainValidationException(ErrorMessage.STATUS_IS_THE_SAME.getMessage());
        }

        switch (newStatus) {
            case ACTIVE -> user.activate();
            case BLOCKED -> user.block();
            default -> throw new DomainValidationException(ErrorMessage.STATUS_INVALID.getMessage());
        }
        userRepository.save(user);

        log.info("User {} status changed to {}", id.value(), newStatus);
    }

    @Override
    public void restore(UserId id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DomainValidationException(ErrorMessage.USER_NOT_FOUND.getMessage()));

        user.restore();

        userRepository.save(user);
    }


    @Override
    public Optional<User> findById(UserId id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByDocument(DocumentId document) {
        return userRepository.findByDocument(document);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(int page, int size) {
        log.info("Fetching all users page={} size={}", page, size);
        return userRepository.findAll(page, size);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAllByStatus(Status status, int page, int size) {
        log.info("Fetching users by status={} page={} size={}", status, page, size);
        return userRepository.findAllByStatus(status, page, size);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAllByRole(Role role, int page, int size) {
        log.info("Fetching users by role={} page={} size={}", role, page, size);
        return userRepository.findAllByRole(role, page, size);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<ProductId> getFavoriteProducts(UserId id) {
        log.info("Fetching favorite products for user={}", id.value());
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DomainValidationException(ErrorMessage.USER_NOT_FOUND.getMessage()));
        return user.getFavoriteProducts();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<CategoryId> getFavoriteCategories(UserId id) {
        log.info("Fetching favorite categories for user={}", id.value());
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DomainValidationException(ErrorMessage.USER_NOT_FOUND.getMessage()));
        return user.getFavoriteCategories();
    }
}
