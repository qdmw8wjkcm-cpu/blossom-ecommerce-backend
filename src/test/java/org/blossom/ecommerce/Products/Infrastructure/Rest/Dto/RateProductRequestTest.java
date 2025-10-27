package org.blossom.ecommerce.Products.Infrastructure.Rest.Dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RateProductRequestTest {

    @Test
    void testRateProductRequest_ConstructionAndAccessors() {
        // Arrange
        Double rating = 4.5;

        // Act
        RateProductRequest request = new RateProductRequest(rating);

        // Assert
        assertEquals(rating, request.rating());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        RateProductRequest request1 = new RateProductRequest(3.0);
        RateProductRequest request2 = new RateProductRequest(3.0);

        // Assert
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }
}