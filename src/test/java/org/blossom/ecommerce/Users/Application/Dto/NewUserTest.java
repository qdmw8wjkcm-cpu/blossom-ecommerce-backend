package org.blossom.ecommerce.Users.Application.Dto;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NewUserTest {

    @Test
    void testNewUser_BuilderAndAccessors() {
        // Arrange
        String name = "John";
        String email = "john.doe@example.com";
        Set<String> favCategories = Set.of("cat1", "cat2");

        // Act
        NewUser newUser = NewUser.builder()
                .name(name)
                .lastname("Doe")
                .password("password123")
                .email(email)
                .document("12345678")
                .phoneNumber("+1234567890")
                .city("New York")
                .country("USA")
                .favoriteCategoriesList(favCategories)
                .favoriteProductsList(Set.of("prod1"))
                .build();

        // Assert
        assertEquals(name, newUser.getName());
        assertEquals("Doe", newUser.getLastname());
        assertEquals("password123", newUser.getPassword());
        assertEquals(email, newUser.getEmail());
        assertEquals("12345678", newUser.getDocument());
        assertEquals("+1234567890", newUser.getPhoneNumber());
        assertEquals("New York", newUser.getCity());
        assertEquals("USA", newUser.getCountry());
        assertEquals(favCategories, newUser.getFavoriteCategoriesList());
        assertEquals(1, newUser.getFavoriteProductsList().size());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        // Arrange
        NewUser newUser = new NewUser();
        String name = "Jane";

        // Act
        newUser.setName(name);
        newUser.setEmail("jane.doe@example.com");

        // Assert
        assertEquals(name, newUser.getName());
        assertEquals("jane.doe@example.com", newUser.getEmail());
    }

    @Test
    void testAllArgsConstructor() {
        // Arrange & Act
        NewUser newUser = new NewUser("Kevin", "Smith", "pass", "kevin@s.com", "doc", "phone", "city", "country", null, null);

        // Assert
        assertEquals("Kevin", newUser.getName());
        assertEquals("pass", newUser.getPassword());
    }
}