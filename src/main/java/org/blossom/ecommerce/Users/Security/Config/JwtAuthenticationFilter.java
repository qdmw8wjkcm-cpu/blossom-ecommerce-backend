package org.blossom.ecommerce.Users.Security.Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.blossom.ecommerce.Users.Security.Service.TokenService;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Order(1)
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokens;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest r) {
        String p = r.getServletPath();
        return p.startsWith("/api/v1/auth/")
                || p.startsWith("/v3/api-docs")
                || p.startsWith("/swagger-ui")
                || p.equals("/swagger-ui.html");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String auth = req.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            try {
                var jwt = tokens.verifyAccess(token);
                String email = jwt.getSubject();
                String roles = String.valueOf(jwt.getClaim("roles"));

                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    var authorities = (roles == null || roles.isBlank())
                            ? List.<SimpleGrantedAuthority>of()
                            : Arrays.stream(roles.split(","))
                            .map(String::trim)
                            .filter(s -> !s.isBlank())
                            .map(SimpleGrantedAuthority::new)
                            .toList();

                    var authToken = new UsernamePasswordAuthenticationToken(email, null, authorities);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception e) {
                log.warn("JWT inválido", e);
            }
        }
        chain.doFilter(req, res);
    }
}