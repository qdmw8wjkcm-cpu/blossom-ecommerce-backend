package org.blossom.ecommerce.Users.Security.Filter;

import lombok.RequiredArgsConstructor;
import org.blossom.ecommerce.Users.Security.Utils.AuthResponse;
import org.blossom.ecommerce.Users.Domain.Ports.Out.UserRepository;
import org.blossom.ecommerce.Users.Domain.ValueObjects.Email;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.blossom.ecommerce.Users.Security.Service.TokenService;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Qualifier("userRepository")
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final TokenService tokenService;

    public AuthResponse login(String email, String rawPassword) {
        var user = users.findByEmail(new Email(email))
                .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));

        var hash = user.getPassword().value();
        if (!encoder.matches(rawPassword, hash)) throw new BadCredentialsException("Credenciales inválidas");

        var role = user.getRole().name();
        var springRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;

        var sub = user.getId().value().toString();
        var access  = tokenService.generateAccessToken(sub, email, springRole);
        var refresh = tokenService.generateRefreshToken(sub, email, role);

        return new AuthResponse(access, refresh);
    }
}