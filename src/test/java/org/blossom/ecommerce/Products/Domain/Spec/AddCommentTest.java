package org.blossom.ecommerce.Products.Domain.Spec;

import org.blossom.ecommerce.Products.Domain.ValueObjects.ProductId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AddCommentTest {

    @Test
    void testAddComment_ConstructionAndAccessors() {
        // Arrange
        ProductId productId = new ProductId(UUID.randomUUID());
        String comment = "This is a test comment.";

        // Act
        AddComment spec = new AddComment(productId, comment);

        // Assert
        assertEquals(productId, spec.id());
        assertEquals(comment, spec.comment());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        ProductId productId = new ProductId(UUID.randomUUID());
        AddComment spec1 = new AddComment(productId, "Comment 1");
        AddComment spec2 = new AddComment(productId, "Comment 1");

        // Assert
        assertEquals(spec1, spec2);
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }
}