package org.blossom.ecommerce.Users.Domain.Models;

import org.blossom.ecommerce.Users.Domain.Enums.Role;
import org.blossom.ecommerce.Users.Domain.Enums.Status;
import org.blossom.ecommerce.Users.Domain.ValueObjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private HashedPassword password;
    private Email email;
    private DocumentId documentId;

    @BeforeEach
    void setUp() {
        password = new HashedPassword("$2a$12$OMjR1g.fiDsPIVlLNH6hoO8XakQKrULTmDorjyQTj.Vm0ur1so7te");
        email = new Email("test@example.com");
        documentId = new DocumentId("12345678");
    }

    @Test
    void testCreateUser_Success() {
        User user = User.create("John", "Doe", password, email, documentId, null, null, null, null, null, Role.ROLE_CUSTOMER);

        assertNotNull(user);
        assertEquals("John", user.getName());
        assertEquals(Status.ACTIVE, user.getStatus());
        assertEquals(Role.ROLE_CUSTOMER, user.getRole());
        assertNotNull(user.getId());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
        assertNull(user.getDeletedAt());
        assertTrue(user.getFavoriteCategories().isEmpty());
        assertTrue(user.getFavoriteProducts().isEmpty());
    }

    @Test
    void testCreateUser_DefaultRoleIsCustomer() {
        User user = User.create("Jane", "Doe", password, email, documentId, null, null, null, null, null, null);
        assertEquals(Role.ROLE_CUSTOMER, user.getRole());
    }

    @Test
    void testChangeEmail_Success() {
        User user = User.create("John", "Doe", password, email, documentId, null, null, null, null, null, Role.ROLE_CUSTOMER);
        Email newEmail = new Email("new.email@example.com");
        user.changeEmail(newEmail);

        assertEquals(newEmail, user.getEmail());
        assertTrue(user.getUpdatedAt().isAfter(user.getCreatedAt()));
    }

    @Test
    void testAddAndRemoveFavoriteProduct() {
        User user = User.create("John", "Doe", password, email, documentId, null, null, null, null, null, Role.ROLE_CUSTOMER);
        ProductId productId = new ProductId(UUID.randomUUID());

        user.addFavoriteProduct(productId);
        assertTrue(user.getFavoriteProducts().contains(productId));

        user.removeFavoriteProduct(productId);
        assertFalse(user.getFavoriteProducts().contains(productId));
    }

    @Test
    void testAddAndRemoveFavoriteCategory() {
        User user = User.create("John", "Doe", password, email, documentId, null, null, null, null, null, Role.ROLE_CUSTOMER);
        CategoryId categoryId = new CategoryId(UUID.randomUUID());

        user.addFavoriteCategory(categoryId);
        assertTrue(user.getFavoriteCategories().contains(categoryId));

        user.removeFavoriteCategory(categoryId);
        assertFalse(user.getFavoriteCategories().contains(categoryId));
    }

    @Test
    void testBlockAndActivateUser() {
        User user = User.create("John", "Doe", password, email, documentId, null, null, null, null, null, Role.ROLE_CUSTOMER);

        user.block();
        assertEquals(Status.BLOCKED, user.getStatus());

        user.activate();
        assertEquals(Status.ACTIVE, user.getStatus());
    }

    @Test
    void testDeleteAndRestoreUser() {
        User user = User.create("John", "Doe", password, email, documentId, null, null, null, null, null, Role.ROLE_CUSTOMER);

        user.delete();
        assertEquals(Status.INACTIVE, user.getStatus());
        assertNotNull(user.getDeletedAt());

        user.restore();
        assertEquals(Status.ACTIVE, user.getStatus());
        assertNull(user.getDeletedAt());
    }

    @Test
    void testRehydrateUser() {
        UserId userId = new UserId(UUID.randomUUID());
        Instant now = Instant.now();
        Set<ProductId> favProducts = new HashSet<>();
        favProducts.add(new ProductId(UUID.randomUUID()));

        User user = User.rehydrate(userId, "Rehydrated", "User", password, email, documentId, null, "City", "Country",
                null, favProducts, Role.ROLE_ADMIN, Status.BLOCKED, now, now, null, "cart-id");

        assertEquals(userId, user.getId());
        assertEquals("Rehydrated", user.getName());
        assertEquals(Role.ROLE_ADMIN, user.getRole());
        assertEquals(Status.BLOCKED, user.getStatus());
        assertEquals(1, user.getFavoriteProducts().size());
    }
}