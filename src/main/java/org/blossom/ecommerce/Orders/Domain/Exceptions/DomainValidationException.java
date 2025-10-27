package org.blossom.ecommerce.Orders.Domain.Exceptions;

public class DomainValidationException extends RuntimeException {
    public DomainValidationException(String message) {
        super(message);
    }
}