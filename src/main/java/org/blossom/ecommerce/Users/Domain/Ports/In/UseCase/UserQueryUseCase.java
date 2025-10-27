package org.blossom.ecommerce.Users.Domain.Ports.In.UseCase;

import org.blossom.ecommerce.Users.Domain.Enums.Role;
import org.blossom.ecommerce.Users.Domain.Enums.Status;
import org.blossom.ecommerce.Users.Domain.Models.User;
import org.blossom.ecommerce.Users.Domain.ValueObjects.*;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.Set;

public interface UserQueryUseCase {
    Optional<User> findById(UserId id);
    Optional<User> findByEmail(Email email);
    Optional<User> findByDocument(DocumentId document);

    Page<User> findAll(int page, int size);
    Page<User> findAllByStatus(Status status, int page, int size);
    Page<User> findAllByRole(Role role, int page, int size);

    Set<ProductId> getFavoriteProducts(UserId id);
    Set<CategoryId> getFavoriteCategories(UserId id);
}