package org.blossom.ecommerce.Users.Application.Dto;

import org.blossom.ecommerce.Users.Domain.ValueObjects.*;

import java.util.Set;

public record UpdateUser(
        UserId userId, HashedPassword password, Email email, String city, String country,
        Set<CategoryId> favoriteCategoriesList, Set<ProductId> favoriteProductsList
) {
}
