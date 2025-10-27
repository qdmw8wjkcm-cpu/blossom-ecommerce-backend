package org.blossom.ecommerce.Users.Application.Dto;

import org.blossom.ecommerce.Users.Domain.ValueObjects.*;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CreateUserSpecTest {

    @Test
    void testCreateUserSpec_ConstructionAndAccessors() {
        // Arrange
        String name = "SpecUser";
        Email email = new Email("spec@example.com");
        DocumentId document = new DocumentId("87654321");
        PhoneNumber phone = new PhoneNumber("123456789");
        Set<CategoryId> favCategories = Set.of(new CategoryId(UUID.randomUUID()));

        // Act
        CreateUserSpec spec = new CreateUserSpec(name, "Last", "pass123", email, document, phone, "City", "Country", favCategories, null);

        // Assert
        assertEquals(name, spec.name());
        assertEquals("Last", spec.lastname());
        assertEquals("pass123", spec.password());
        assertEquals(email, spec.email());
        assertEquals(document, spec.document());
        assertEquals(phone, spec.phoneNumber());
        assertEquals("City", spec.city());
        assertEquals("Country", spec.country());
        assertEquals(favCategories, spec.favoriteCategories());
        assertNull(spec.favoriteProducts());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        Email email = new Email("spec@example.com");
        DocumentId document = new DocumentId("87654321");

        CreateUserSpec spec1 = new CreateUserSpec("Spec", "User", "p", email, document, null, null, null, null, null);
        CreateUserSpec spec2 = new CreateUserSpec("Spec", "User", "p", email, document, null, null, null, null, null);

        // Assert
        assertEquals(spec1, spec2);
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }
}