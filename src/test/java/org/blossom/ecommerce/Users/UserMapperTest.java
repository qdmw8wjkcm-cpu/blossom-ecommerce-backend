package org.blossom.ecommerce.Users;

import org.blossom.ecommerce.Users.Adapters.Jpa.UserEntity;
import org.blossom.ecommerce.Users.Adapters.Mappers.UserMapper;
import org.blossom.ecommerce.Users.Domain.Enums.Role;
import org.blossom.ecommerce.Users.Domain.Enums.Status;
import org.blossom.ecommerce.Users.Domain.Models.User;
import org.blossom.ecommerce.Users.Domain.ValueObjects.*;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void toEntity_Success() {
        User user = User.create(
                "John",
                "Doe",
                HashedPassword.fromRaw("$2a$12$OMjR1g.fiDsPIVlLNH6hoO8XakQKrULTmDorjyQTj.Vm0ur1so7te"),
                new Email("test@example.com"),
                new DocumentId("12345678"),
                new PhoneNumber("+573001234567"),
                "Bogota",
                "Colombia",
                Set.of(new CategoryId(UUID.randomUUID())),
                Set.of(new ProductId(UUID.randomUUID())),
                Role.ROLE_CUSTOMER
        );

        UserEntity entity = UserMapper.toEntity(user);

        assertNotNull(entity);
        assertEquals(user.getId().value(), entity.getId());
        assertEquals(user.getName(), entity.getName());
        assertEquals(user.getLastname(), entity.getLastname());
        assertEquals(user.getPassword().value(), entity.getPassword());
        assertEquals(user.getEmail().value(), entity.getEmail());
        assertEquals(user.getDocument().document(), entity.getDocument());
        assertEquals(user.getPhoneNumber().value(), entity.getPhoneNumber());
        assertEquals(user.getCity(), entity.getCity());
        assertEquals(user.getCountry(), entity.getCountry());
        assertEquals(user.getRole(), entity.getRole());
        assertEquals(user.getStatus(), entity.getStatus());
        assertEquals(1, entity.getFavoriteCategoriesList().size());
        assertEquals(1, entity.getFavoriteProductsList().size());
    }

    @Test
    void toEntity_NullUser() {
        UserEntity entity = UserMapper.toEntity(null);
        assertNull(entity);
    }

    @Test
    void toEntity_WithoutPhoneNumber() {
        User user = User.create(
                "John",
                "Doe",
                HashedPassword.fromRaw("$2a$12$OMjR1g.fiDsPIVlLNH6hoO8XakQKrULTmDorjyQTj.Vm0ur1so7te"),
                new Email("test@example.com"),
                new DocumentId("12345678"),
                null,
                "Bogota",
                "Colombia",
                Set.of(),
                Set.of(),
                Role.ROLE_CUSTOMER
        );

        UserEntity entity = UserMapper.toEntity(user);

        assertNotNull(entity);
        assertNull(entity.getPhoneNumber());
    }

    @Test
    void toDomain_Success() {
        UserEntity entity = new UserEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("John");
        entity.setLastname("Doe");
        entity.setPassword("$2a$12$OMjR1g.fiDsPIVlLNH6hoO8XakQKrULTmDorjyQTj.Vm0ur1so7te");
        entity.setEmail("test@example.com");
        entity.setDocument("12345678");
        entity.setPhoneNumber("+573001234567");
        entity.setCity("Bogota");
        entity.setCountry("Colombia");
        entity.setRole(Role.ROLE_CUSTOMER);
        entity.setStatus(Status.ACTIVE);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        entity.setCartId(UUID.randomUUID().toString());
        entity.setFavoriteCategoriesList(Set.of(UUID.randomUUID().toString()));
        entity.setFavoriteProductsList(Set.of(UUID.randomUUID().toString()));

        User user = UserMapper.toDomain(entity);

        assertNotNull(user);
        assertEquals(entity.getId(), user.getId().value());
        assertEquals(entity.getName(), user.getName());
        assertEquals(entity.getLastname(), user.getLastname());
        assertEquals(entity.getPassword(), user.getPassword().value());
        assertEquals(entity.getEmail(), user.getEmail().value());
        assertEquals(entity.getDocument(), user.getDocument().document());
        assertEquals(entity.getPhoneNumber(), user.getPhoneNumber().value());
        assertEquals(entity.getCity(), user.getCity());
        assertEquals(entity.getCountry(), user.getCountry());
        assertEquals(entity.getRole(), user.getRole());
        assertEquals(entity.getStatus(), user.getStatus());
        assertEquals(1, user.getFavoriteCategories().size());
        assertEquals(1, user.getFavoriteProducts().size());
    }

    @Test
    void toDomain_NullEntity() {
        User user = UserMapper.toDomain(null);
        assertNull(user);
    }

    @Test
    void toDomain_WithoutPhoneNumber() {
        UserEntity entity = new UserEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("John");
        entity.setLastname("Doe");
        entity.setPassword("$2a$12$OMjR1g.fiDsPIVlLNH6hoO8XakQKrULTmDorjyQTj.Vm0ur1so7te");
        entity.setEmail("test@example.com");
        entity.setDocument("12345678");
        entity.setPhoneNumber(null);
        entity.setCity("Bogota");
        entity.setCountry("Colombia");
        entity.setRole(Role.ROLE_CUSTOMER);
        entity.setStatus(Status.ACTIVE);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        entity.setCartId(UUID.randomUUID().toString());
        entity.setFavoriteCategoriesList(Set.of());
        entity.setFavoriteProductsList(Set.of());

        User user = UserMapper.toDomain(entity);

        assertNotNull(user);
        assertNull(user.getPhoneNumber());
    }

    @Test
    void roundTrip_ToEntityAndBackToDomain() {
        User originalUser = User.create(
                "John",
                "Doe",
                HashedPassword.fromRaw("$2a$12$OMjR1g.fiDsPIVlLNH6hoO8XakQKrULTmDorjyQTj.Vm0ur1so7te"),
                new Email("test@example.com"),
                new DocumentId("12345678"),
                new PhoneNumber("+573001234567"),
                "Bogota",
                "Colombia",
                Set.of(),
                Set.of(),
                Role.ROLE_CUSTOMER
        );

        UserEntity entity = UserMapper.toEntity(originalUser);
        User mappedUser = UserMapper.toDomain(entity);

        assertEquals(originalUser.getId().value(), mappedUser.getId().value());
        assertEquals(originalUser.getName(), mappedUser.getName());
        assertEquals(originalUser.getEmail().value(), mappedUser.getEmail().value());
        assertEquals(originalUser.getDocument().document(), mappedUser.getDocument().document());
    }
}