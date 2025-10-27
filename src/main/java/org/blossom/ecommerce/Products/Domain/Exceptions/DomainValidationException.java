package org.blossom.ecommerce.Products.Domain.Exceptions;

public class DomainValidationException extends RuntimeException {
    public DomainValidationException(String message) {
        super(message);
    }
}