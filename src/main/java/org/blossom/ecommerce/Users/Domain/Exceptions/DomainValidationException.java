package org.blossom.ecommerce.Users.Domain.Exceptions;

public class DomainValidationException extends RuntimeException {
    public DomainValidationException(String message) {
        super(message);
    }
}