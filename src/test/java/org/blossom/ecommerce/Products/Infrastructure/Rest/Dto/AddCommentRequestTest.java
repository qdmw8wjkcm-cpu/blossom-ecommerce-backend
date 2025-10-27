package org.blossom.ecommerce.Products.Infrastructure.Rest.Dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddCommentRequestTest {

    @Test
    void testAddCommentRequest_ConstructionAndAccessors() {
        // Arrange
        String comment = "This is a test comment.";

        // Act
        AddCommentRequest request = new AddCommentRequest(comment);

        // Assert
        assertEquals(comment, request.comment());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        AddCommentRequest request1 = new AddCommentRequest("Comment 1");
        AddCommentRequest request2 = new AddCommentRequest("Comment 1");

        // Assert
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }
}