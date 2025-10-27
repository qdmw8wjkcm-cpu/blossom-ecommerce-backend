package org.blossom.ecommerce.Users.Security.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtils {

    private final Algorithm algorithm;
    private final long accessExpMs;
    private final long refreshExpMs;

    public JWTUtils(SecurityConstants cfg) {
        this.algorithm = Algorithm.HMAC256(cfg.getSecret());
        this.accessExpMs = cfg.getAccessTokenExpiration();
        this.refreshExpMs = cfg.getRefreshTokenExpiration();
    }

    public String generateAccess(String id, String email, String role) {
        long now = System.currentTimeMillis();
        return JWT.create()
                .withSubject(email)
                .withClaim("id", id)
                .withClaim("role", role)
                .withIssuedAt(new Date(now))
                .withExpiresAt(new Date(now + accessExpMs))
                .sign(algorithm);
    }

    public String generateRefresh(String id, String email, String role) {
        long now = System.currentTimeMillis();
        return JWT.create()
                .withSubject(email)
                .withClaim("id", id)
                .withClaim("role", role)
                .withIssuedAt(new Date(now))
                .withExpiresAt(new Date(now + refreshExpMs))
                .sign(algorithm);
    }

    public DecodedJWT verify(String token) {
        return JWT.require(algorithm).build().verify(token);
    }
}