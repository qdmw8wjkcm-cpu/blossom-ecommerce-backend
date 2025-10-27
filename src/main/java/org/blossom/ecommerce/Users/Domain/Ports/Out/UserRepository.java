package org.blossom.ecommerce.Users.Domain.Ports.Out;

import org.blossom.ecommerce.Users.Domain.Enums.Role;
import org.blossom.ecommerce.Users.Domain.Enums.Status;
import org.springframework.data.domain.Page;
import org.blossom.ecommerce.Users.Domain.Models.User;
import org.blossom.ecommerce.Users.Domain.ValueObjects.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository {
    User save(User user);

    Optional<User> findById(UserId id);
    Optional<User> findByEmail(Email email);
    Optional<User> findByDocument(DocumentId document);

    Page<User> findAll(int page, int size);
    Page<User> findAllByStatus(Status status, int page, int size);
    Page<User> findAllByRole(Role role, int page, int size);

}