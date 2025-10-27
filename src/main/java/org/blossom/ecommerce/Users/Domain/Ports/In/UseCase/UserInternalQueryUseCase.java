package org.blossom.ecommerce.Users.Domain.Ports.In.UseCase;

import org.blossom.ecommerce.Users.Domain.Enums.Status;
import org.blossom.ecommerce.Users.Domain.Models.User;
import org.blossom.ecommerce.Users.Domain.ValueObjects.*;

import java.util.Optional;

public interface UserInternalQueryUseCase {
    boolean existsById(UserId id);
    boolean existsByEmail(Email email);
    Status getStatus(UserId id);
    Optional<User> findByEmail(Email email);

}