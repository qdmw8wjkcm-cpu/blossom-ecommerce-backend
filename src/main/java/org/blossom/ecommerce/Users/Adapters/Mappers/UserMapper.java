package org.blossom.ecommerce.Users.Adapters.Mappers;

import org.blossom.ecommerce.Users.Adapters.Jpa.UserEntity;
import org.blossom.ecommerce.Users.Domain.Models.User;
import org.blossom.ecommerce.Users.Domain.ValueObjects.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setId(user.getId().value());
        entity.setName(user.getName());
        entity.setLastname(user.getLastname());
        entity.setPassword(user.getPassword().value());
        entity.setEmail(user.getEmail().value());
        entity.setDocument(user.getDocument().document());
        entity.setPhoneNumber(user.getPhoneNumber() != null ? user.getPhoneNumber().value() : null);
        entity.setCity(user.getCity());
        entity.setCountry(user.getCountry());
        entity.setCartId(user.getCart());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());
        entity.setDeletedAt(user.getDeletedAt());
        entity.setRole(user.getRole());
        entity.setStatus(user.getStatus());

        entity.setFavoriteCategoriesList(
                user.getFavoriteCategories().stream()
                        .map(categoryId -> categoryId.value().toString())
                        .collect(Collectors.toSet())
        );

        entity.setFavoriteProductsList(
                user.getFavoriteProducts().stream()
                        .map(productId -> productId.value().toString())
                        .collect(Collectors.toSet())
        );
        return entity;
    }

    public static User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        Set<CategoryId> favoriteCategories = entity.getFavoriteCategoriesList().stream()
                .map(str -> new CategoryId(UUID.fromString(str)))
                .collect(Collectors.toSet());

        Set<ProductId> favoriteProducts = entity.getFavoriteProductsList().stream()
                .map(str -> new ProductId(UUID.fromString(str)))
                .collect(Collectors.toSet());

        return User.rehydrate(
                new UserId(entity.getId()),
                entity.getName(),
                entity.getLastname(),
                new HashedPassword(entity.getPassword()),
                new Email(entity.getEmail()),
                new DocumentId(entity.getDocument()),
                entity.getPhoneNumber() != null ? new PhoneNumber(entity.getPhoneNumber()) : null,
                entity.getCity(),
                entity.getCountry(),
                favoriteCategories,
                favoriteProducts,
                entity.getRole(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt(),
                entity.getCartId()
        );
    }
}