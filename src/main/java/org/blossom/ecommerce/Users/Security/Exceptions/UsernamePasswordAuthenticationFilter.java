package org.blossom.ecommerce.Users.Security.Exceptions;

public class UsernamePasswordAuthenticationFilter extends RuntimeException{
    public UsernamePasswordAuthenticationFilter(String message){
        super(message);
    }
}
