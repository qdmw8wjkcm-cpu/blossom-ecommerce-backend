package org.blossom.ecommerce.Users.Security.Utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
@RequiredArgsConstructor
public class SecurityConstants {

    @Value("${JWT_SECRET}")
    private String secret;

    @Value("${app.jwt.access-expiration:900000}")
    private long accessTokenExpiration;

    @Value("${app.jwt.refresh-expiration:604800000}")
    private long refreshTokenExpiration;

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}