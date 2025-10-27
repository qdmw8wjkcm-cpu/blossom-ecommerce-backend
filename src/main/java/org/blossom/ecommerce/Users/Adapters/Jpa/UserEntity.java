package org.blossom.ecommerce.Users.Adapters.Jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.blossom.ecommerce.Users.Domain.Enums.Role;
import org.blossom.ecommerce.Users.Domain.Enums.Status;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users",
        indexes = {
                @Index(name = "ix_users_email", columnList = "email", unique = true),
                @Index(name = "ix_users_document", columnList = "document", unique = true)
        })
@Getter
@Setter
public class UserEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true, length = 10)
    private String document;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    private String city;
    private String country;

    @ElementCollection
    @CollectionTable(name = "user_favorite_categories", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "category_id", length = 36, nullable = false)
    private Set<String> favoriteCategoriesList = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "user_favorite_products", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "product_id", length = 36, nullable = false)
    private Set<String> favoriteProductsList = new HashSet<>();

    @Column(name = "cart_id", length = 36)
    private String cartId;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    private Instant deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

}