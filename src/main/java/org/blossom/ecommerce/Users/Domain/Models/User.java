package org.blossom.ecommerce.Users.Domain.Models;

import org.blossom.ecommerce.Users.Domain.Enums.Role;
import org.blossom.ecommerce.Users.Domain.Enums.Status;
import org.blossom.ecommerce.Users.Domain.ValueObjects.*;

import java.time.Instant;
import java.util.*;

public class User {

    private UserId id;
    private String name;
    private String lastname;
    private HashedPassword password;
    private Email email;
    private DocumentId document;
    private PhoneNumber phoneNumber;
    private String city;
    private String country;
    private Set<CategoryId> favoriteCategoriesList;
    private Set<ProductId> favoriteProductsList;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    private Role role;
    private Status status;
    private String cart;

    public static User create( String name, String lastname, HashedPassword password,
                              Email email, DocumentId document, PhoneNumber phoneNumber,
                              String city, String country,
                              Set<CategoryId> favoriteCategories, Set<ProductId> favoriteProducts,
                              Role role) {

        return new User(
                new UserId(UUID.randomUUID()),
                name,
                lastname,
                password,
                email,
                document,
                phoneNumber,
                city,
                country,
                favoriteCategories,
                favoriteProducts,
                role != null ? role : Role.ROLE_CUSTOMER,
                Status.ACTIVE,
                Instant.now(),
                Instant.now(),
                null,
                UUID.randomUUID().toString()
        );
    }

    public static User rehydrate(
            UserId id, String name, String lastname, HashedPassword password, Email email,
            DocumentId document, PhoneNumber phoneNumber, String city, String country,
            Set<CategoryId> favoriteCategories, Set<ProductId> favoriteProducts,
            Role role, Status status, Instant createdAt, Instant updatedAt, Instant deletedAt, String cart
    ) {
        return new User(id, name, lastname, password, email, document, phoneNumber, city, country,
                favoriteCategories, favoriteProducts, role, status, createdAt, updatedAt, deletedAt, cart);
    }

    private User(UserId id,
                 String name,
                 String lastname,
                 HashedPassword password,
                 Email email,
                 DocumentId document,
                 PhoneNumber phoneNumber,
                 String city,
                 String country,
                 Set<CategoryId> favoriteCategories,
                 Set<ProductId> favoriteProducts,
                 Role role,
                 Status status,
                 Instant createdAt,
                 Instant updatedAt,
                 Instant deletedAt,
                 String cart) {

        this.id = Objects.requireNonNull(id, "id");
        this.name = Objects.requireNonNull(name, "name");
        this.lastname = Objects.requireNonNull(lastname, "lastname");
        this.password = Objects.requireNonNull(password, "password");
        this.email = Objects.requireNonNull(email, "email");
        this.document = Objects.requireNonNull(document, "document");
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.country = country;
        this.favoriteCategoriesList = new HashSet<>(Optional.ofNullable(favoriteCategories).orElseGet(Set::of));
        this.favoriteProductsList = new HashSet<>(Optional.ofNullable(favoriteProducts).orElseGet(Set::of));
        this.cart = cart;
        this.deletedAt = deletedAt;
        this.role = Objects.requireNonNullElse(role, Role.ROLE_CUSTOMER);
        this.status = Objects.requireNonNullElse(status, Status.ACTIVE);
        this.createdAt = Objects.requireNonNullElseGet(createdAt, Instant::now);
        this.updatedAt = updatedAt != null ? updatedAt : this.createdAt;
    }


    public UserId getId() { return id; }
    public String getName() { return name; }
    public String getLastname() { return lastname; }
    public HashedPassword getPassword() { return password; }
    public Email getEmail() { return email; }
    public DocumentId getDocument() { return document; }
    public PhoneNumber getPhoneNumber() { return phoneNumber; }
    public String getCity() { return city; }
    public String getCountry() { return country; }
    public Role getRole() { return role; }
    public Status getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Instant getDeletedAt() { return deletedAt; }
    public String getCart() { return cart;}

    public Set<CategoryId> getFavoriteCategories() {
        return Collections.unmodifiableSet(favoriteCategoriesList);
    }

    public Set<ProductId> getFavoriteProducts() {
        return Collections.unmodifiableSet(favoriteProductsList);
    }


    public void changeEmail(Email newEmail) {
        this.email = Objects.requireNonNull(newEmail);
        touch();
    }

    public void changePassword(HashedPassword newHashedPassword) {
        this.password = Objects.requireNonNull(newHashedPassword);
        touch();
    }

    public void changePhoneNumber(PhoneNumber newPhoneNumber) {
        this.phoneNumber = Objects.requireNonNull(newPhoneNumber);
        touch();
    }

    public void changeCity(String city) {
        this.city = Objects.requireNonNull(city);
        touch();
    }

    public void changeCountry(String country) {
        this.country = Objects.requireNonNull(country);
        touch();
    }

    public void addFavoriteProduct(ProductId id) {
        if (favoriteProductsList.add(Objects.requireNonNull(id))) touch();
    }

    public void removeFavoriteProduct(ProductId id) {
        if (favoriteProductsList.remove(Objects.requireNonNull(id))) touch();
    }

    public void addFavoriteCategory(CategoryId id) {
        if (favoriteCategoriesList.add(Objects.requireNonNull(id))) touch();
    }

    public void removeFavoriteCategory(CategoryId id) {
        if (favoriteCategoriesList.remove(Objects.requireNonNull(id))) touch();
    }


    public void block() {
        this.status = Status.BLOCKED;
        touch();
    }

    public void activate() {
        this.status = Status.ACTIVE;
        touch();
    }

    public void delete() {
        this.deletedAt = Instant.now();
        this.status = Status.INACTIVE;
        touch();
    }

    public void restore() {
        this.deletedAt = null;
        this.status = Status.ACTIVE;
        touch();
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }
}