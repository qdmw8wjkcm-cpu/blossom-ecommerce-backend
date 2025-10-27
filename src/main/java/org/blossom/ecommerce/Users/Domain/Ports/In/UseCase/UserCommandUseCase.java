package org.blossom.ecommerce.Users.Domain.Ports.In.UseCase;

import org.blossom.ecommerce.Users.Application.Dto.CreateUserSpec;
import org.blossom.ecommerce.Users.Application.Dto.UpdateUser;
import org.blossom.ecommerce.Users.Domain.Enums.Status;
import org.blossom.ecommerce.Users.Domain.Models.User;
import org.blossom.ecommerce.Users.Domain.ValueObjects.*;

import java.util.Optional;

public interface UserCommandUseCase {
    Optional<User> createProfile(CreateUserSpec newUser);
    User updateProfile(UpdateUser cmd);

    void changeEmail(UserId id, Email email);
    void changePassword(UserId id, HashedPassword password);
    void changePhone(UserId id, PhoneNumber phone);
    void changeCity(UserId id, String city);
    void changeCountry(UserId id, String country);

    void addFavoriteProduct(UserId userId, ProductId productId);
    void removeFavoriteProduct(UserId userId, ProductId productId);
    void addFavoriteCategory(UserId userId, CategoryId categoryId);
    void removeFavoriteCategory(UserId userId, CategoryId categoryId);

    void changeStatus(UserId id, Status status);
    void restore(UserId id);
}