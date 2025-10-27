package org.blossom.ecommerce.Users.Application.Service;

import org.blossom.ecommerce.Users.Application.Dto.CreateUserSpec;
import org.blossom.ecommerce.Users.Domain.Enums.ErrorMessage;
import org.blossom.ecommerce.Users.Domain.Exceptions.DomainValidationException;
import org.blossom.ecommerce.Users.Domain.Models.User;
import org.blossom.ecommerce.Users.Domain.Ports.Out.UserRepository;
import org.blossom.ecommerce.Users.Domain.ValueObjects.DocumentId;
import org.blossom.ecommerce.Users.Domain.ValueObjects.Email;
import org.blossom.ecommerce.Users.Domain.ValueObjects.HashedPassword;
import org.blossom.ecommerce.Users.Domain.ValueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    private CreateUserSpec createUserSpec;
    private Email email;
    private DocumentId documentId;

    @BeforeEach
    void setUp() {
        email = new Email("test@example.com");
        documentId = new DocumentId("12345678");
        createUserSpec = new CreateUserSpec(
                "John",
                "Doe",
                "raw-password",
                email,
                documentId,
                null, null, null, null, null
        );
    }

    @Test
    void testCreateProfile_Success() {
        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.empty());
        when(userRepository.findByDocument(any(DocumentId.class))).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<User> createdUserOpt = userService.createProfile(createUserSpec);

        assertTrue(createdUserOpt.isPresent());
        User createdUser = createdUserOpt.get();
        assertEquals("John", createdUser.getName());
        assertEquals(email, createdUser.getEmail());

        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(1)).findByDocument(documentId);
        verify(passwordEncoder, times(1)).encode("raw-password");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateProfile_EmailAlreadyExists_ShouldThrowException() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mock(User.class)));

        DomainValidationException exception = assertThrows(DomainValidationException.class, () -> {
            userService.createProfile(createUserSpec);
        });

        assertEquals(ErrorMessage.USER_EXIST.getMessage(), exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCreateProfile_DocumentAlreadyExists_ShouldThrowException() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.findByDocument(documentId)).thenReturn(Optional.of(mock(User.class)));

        DomainValidationException exception = assertThrows(DomainValidationException.class, () -> {
            userService.createProfile(createUserSpec);
        });

        assertEquals(ErrorMessage.USER_EXIST.getMessage(), exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testChangeEmail_Success() {
        UserId userId = new UserId(UUID.randomUUID());
        User existingUser = User.create("Test", "User", new HashedPassword("$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy"),
                new Email("old@example.com"), new DocumentId("87654321"), null, null, null, null, null, null);
        Email newEmail = new Email("new@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        userService.changeEmail(userId, newEmail);

        assertEquals(newEmail, existingUser.getEmail());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testChangeEmail_UserNotFound_ShouldThrowException() {
        UserId userId = new UserId(UUID.randomUUID());
        Email newEmail = new Email("new@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(DomainValidationException.class, () -> {
            userService.changeEmail(userId, newEmail);
        });

        verify(userRepository, never()).save(any(User.class));
    }
}