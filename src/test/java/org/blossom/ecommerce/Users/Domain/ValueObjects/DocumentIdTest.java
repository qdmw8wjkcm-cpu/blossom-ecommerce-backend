package org.blossom.ecommerce.Users.Domain.ValueObjects;

import org.blossom.ecommerce.Users.Domain.Enums.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class DocumentIdTest {

    @ParameterizedTest
    @ValueSource(strings = {"12345", "1234567890"})
    void testCreateDocumentId_ValidDocument_Success(String validDocument) {
        DocumentId documentId = new DocumentId(validDocument);
        assertNotNull(documentId);
        assertEquals(validDocument, documentId.document());
    }

    @Test
    void testCreateDocumentId_NullDocument_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> {
            new DocumentId(null);
        });
    }

    @Test
    void testCreateDocumentId_BlankDocument_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new DocumentId("   ");
        });
        assertEquals(ErrorMessage.DOCUMENT_BLANK.getMessage(), exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234", "12345678901"})
    void testCreateDocumentId_InvalidLength_ShouldThrowException(String invalidLengthDoc) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new DocumentId(invalidLengthDoc);
        });
        assertEquals(ErrorMessage.DOCUMENT_INVALID_LENGTH.getMessage(), exception.getMessage());
    }

    @Test
    void testCreateDocumentId_NonNumeric_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new DocumentId("123a567");
        });
        assertEquals(ErrorMessage.DOCUMENT_NOT_NUMERIC.getMessage(), exception.getMessage());
    }
}