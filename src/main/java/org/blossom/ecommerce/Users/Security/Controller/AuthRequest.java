package org.blossom.ecommerce.Users.Security.Controller;

public record AuthRequest(
        String email,
        String password
) {

}
