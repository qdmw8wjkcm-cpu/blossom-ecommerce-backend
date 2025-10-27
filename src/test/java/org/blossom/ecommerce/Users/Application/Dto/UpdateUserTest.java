package org.blossom.ecommerce.Users.Application.Dto;

import org.blossom.ecommerce.Users.Domain.ValueObjects.*;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UpdateUserTest {

    @Test
    void testUpdateUser_ConstructionAndAccessors() {
        // Arrange
        UserId userId = new UserId(UUID.randomUUID());
        HashedPassword password = new HashedPassword("$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy");
        Email email = new Email("update@example.com");
        String city = "London";
        String country = "UK";
        Set<CategoryId> favCategories = Set.of(new CategoryId(UUID.randomUUID()));
        Set<ProductId> favProducts = Set.of(new ProductId(UUID.randomUUID()));

        // Act
        UpdateUser updateUser = new UpdateUser(userId, password, email, city, country, favCategories, favProducts);

        // Assert
        assertEquals(userId, updateUser.userId());
        assertEquals(password, updateUser.password());
        assertEquals(email, updateUser.email());
        assertEquals(city, updateUser.city());
        assertEquals(country, updateUser.country());
        assertEquals(favCategories, updateUser.favoriteCategoriesList());
        assertEquals(favProducts, updateUser.favoriteProductsList());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        UserId userId = new UserId(UUID.randomUUID());
        UpdateUser spec1 = new UpdateUser(userId, null, null, "City", null, null, null);
        UpdateUser spec2 = new UpdateUser(userId, null, null, "City", null, null, null);

        // Assert
        assertEquals(spec1, spec2);
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }
}