package org.blossom.ecommerce.Users.Security.Service;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.blossom.ecommerce.Users.Security.Utils.JWTUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JWTUtils jwt;

    public String generateAccessToken(String id, String email, String role) {
        return jwt.generateAccess(id, email, role);
    }

    public String generateRefreshToken(String id, String email, String role) {
        return jwt.generateRefresh(id, email, role);
    }

    public DecodedJWT verifyAccess(String token) {
        return jwt.verify(token);
    }
}