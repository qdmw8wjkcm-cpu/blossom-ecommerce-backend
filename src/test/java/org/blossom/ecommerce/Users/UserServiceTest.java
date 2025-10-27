package org.blossom.ecommerce.Users;

import org.blossom.ecommerce.Users.Application.Dto.CreateUserSpec;
import org.blossom.ecommerce.Users.Application.Dto.UpdateUser;
import org.blossom.ecommerce.Users.Application.Service.UserService;
import org.blossom.ecommerce.Users.Domain.Enums.ErrorMessage;
import org.blossom.ecommerce.Users.Domain.Enums.Role;
import org.blossom.ecommerce.Users.Domain.Enums.Status;
import org.blossom.ecommerce.Users.Domain.Exceptions.DomainValidationException;
import org.blossom.ecommerce.Users.Domain.Models.User;
import org.blossom.ecommerce.Users.Domain.Ports.Out.UserRepository;
import org.blossom.ecommerce.Users.Domain.ValueObjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private CreateUserSpec createUserSpec;
    private User user;
    private UserId userId;
    private Email email;

    @BeforeEach
    void setUp() {
        userId = new UserId(UUID.randomUUID());
        email = new Email("test@example.com");

        createUserSpec = new CreateUserSpec(
                "John",
                "Doe",
                "password123",
                email,
                new DocumentId("12345678"),
                new PhoneNumber("+573001234567"),
                "Bogota",
                "Colombia",
                Set.of(),
                Set.of()
        );

        user = User.create(
                "John",
                "Doe",
                HashedPassword.fromRaw("$2a$12$OMjR1g.fiDsPIVlLNH6hoO8XakQKrULTmDorjyQTj.Vm0ur1so7te"),
                email,
                new DocumentId("12345678"),
                new PhoneNumber("+573001234567"),
                "Bogota",
                "Colombia",
                Set.of(),
                Set.of(),
                Role.ROLE_CUSTOMER
        );
    }

    @Test
    void createProfile_Success() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.findByDocument(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("$2a$12$OMjR1g.fiDsPIVlLNH6hoO8XakQKrULTmDorjyQTj.Vm0ur1so7te");
        when(userRepository.save(any())).thenReturn(user);

        Optional<User> result = userService.createProfile(createUserSpec);

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getName());
        verify(userRepository).save(any());
    }

    @Test
    void createProfile_EmailAlreadyExists() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));

        DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> userService.createProfile(createUserSpec)
        );

        assertEquals(ErrorMessage.USER_EXIST.getMessage(), exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void createProfile_DocumentAlreadyExists() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.findByDocument(any())).thenReturn(Optional.of(user));

        DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> userService.createProfile(createUserSpec)
        );

        assertEquals(ErrorMessage.USER_EXIST.getMessage(), exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void changeEmail_Success() {
        Email newEmail = new Email("newemail@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        userService.changeEmail(userId, newEmail);

        verify(userRepository).save(any());
    }

    @Test
    void changeEmail_SameEmail() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> userService.changeEmail(userId, email)
        );

        assertEquals(ErrorMessage.EMAIL_IS_THE_SAME.getMessage(), exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void changeEmail_UserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(DomainValidationException.class,
                () -> userService.changeEmail(userId, new Email("new@example.com"))
        );
    }

    @Test
    void updateProfile_Success() {
        UpdateUser updateUser = new UpdateUser(
                userId,
                null,
                new Email("updated@example.com"),
                "Medellin",
                "Colombia",
                null,
                null
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);

        User result = userService.updateProfile(updateUser);

        assertNotNull(result);
        verify(userRepository).save(any());
    }

    @Test
    void updateProfile_EmailInUse() {
        User anotherUser = User.create(
                "Jane",
                "Smith",
                HashedPassword.fromRaw("$2a$12$OMjR1g.fiDsPIVlLNH6hoO8XakQKrULTmDorjyQTj.Vm0ur1so7te"),
                new Email("other@example.com"),
                new DocumentId("87654321"),
                null,
                null,
                null,
                Set.of(),
                Set.of(),
                Role.ROLE_CUSTOMER
        );

        UpdateUser updateUser = new UpdateUser(
                userId,
                null,
                new Email("other@example.com"),
                null,
                null,
                null,
                null
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(anotherUser));

        assertThrows(DomainValidationException.class,
                () -> userService.updateProfile(updateUser)
        );
    }

    @Test
    void addFavoriteProduct_Success() {
        ProductId productId = new ProductId(UUID.randomUUID());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        userService.addFavoriteProduct(userId, productId);

        verify(userRepository).save(any());
    }

    @Test
    void addFavoriteProduct_AlreadyExists() {
        ProductId productId = new ProductId(UUID.randomUUID());
        user.addFavoriteProduct(productId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(DomainValidationException.class,
                () -> userService.addFavoriteProduct(userId, productId)
        );
    }

    @Test
    void removeFavoriteProduct_Success() {
        ProductId productId = new ProductId(UUID.randomUUID());
        user.addFavoriteProduct(productId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        userService.removeFavoriteProduct(userId, productId);

        verify(userRepository).save(any());
    }

    @Test
    void addFavoriteCategory_Success() {
        CategoryId categoryId = new CategoryId(UUID.randomUUID());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        userService.addFavoriteCategory(userId, categoryId);

        verify(userRepository).save(any());
    }

    @Test
    void changeStatus_ToBlocked() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        userService.changeStatus(userId, Status.BLOCKED);

        verify(userRepository).save(any());
    }

    @Test
    void changeStatus_SameStatus() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(DomainValidationException.class,
                () -> userService.changeStatus(userId, Status.ACTIVE)
        );
    }

    @Test
    void findById_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findById(userId);

        assertTrue(result.isPresent());
        assertEquals(user.getName(), result.get().getName());
    }

    @Test
    void findByEmail_Success() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByEmail(email);

        assertTrue(result.isPresent());
    }

    @Test
    void findAll_Success() {
        List<User> users = List.of(user);
        Page<User> page = new PageImpl<>(users);

        when(userRepository.findAll(0, 20)).thenReturn(page);

        Page<User> result = userService.findAll(0, 20);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findAllByStatus_Success() {
        List<User> users = List.of(user);
        Page<User> page = new PageImpl<>(users);

        when(userRepository.findAllByStatus(Status.ACTIVE, 0, 20)).thenReturn(page);

        Page<User> result = userService.findAllByStatus(Status.ACTIVE, 0, 20);

        assertFalse(result.isEmpty());
    }

    @Test
    void getFavoriteProducts_Success() {
        ProductId productId = new ProductId(UUID.randomUUID());
        user.addFavoriteProduct(productId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Set<ProductId> result = userService.getFavoriteProducts(userId);

        assertFalse(result.isEmpty());
        assertTrue(result.contains(productId));
    }

    @Test
    void getFavoriteCategories_Success() {
        CategoryId categoryId = new CategoryId(UUID.randomUUID());
        user.addFavoriteCategory(categoryId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Set<CategoryId> result = userService.getFavoriteCategories(userId);

        assertFalse(result.isEmpty());
        assertTrue(result.contains(categoryId));
    }

    @Test
    void restore_Success() {
        user.delete();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        userService.restore(userId);

        verify(userRepository).save(any());
    }
}