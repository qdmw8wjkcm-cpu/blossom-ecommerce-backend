package org.blossom.ecommerce.Users.Security.Controller;

import lombok.RequiredArgsConstructor;
import org.blossom.ecommerce.Users.Security.Filter.AuthService;
import org.blossom.ecommerce.Users.Security.Utils.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService auth;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse>login(@RequestBody AuthRequest req) {
        return ResponseEntity.ok(auth.login(req.email(), req.password()));
    }

}