package org.blossom.ecommerce.Users.Domain.ValueObjects;

import org.blossom.ecommerce.Users.Domain.Enums.ErrorMessage;

import java.util.Objects;
import java.util.regex.Pattern;

public record DocumentId(String document) {

    private static final Pattern PATTERN = Pattern.compile("^\\d+$");

    public DocumentId{

        Objects.requireNonNull(document);

        if (document.isBlank()) throw new IllegalArgumentException(ErrorMessage.DOCUMENT_BLANK.getMessage());

        if (document.length() < 5 || document.length() > 10) throw new IllegalArgumentException(ErrorMessage.DOCUMENT_INVALID_LENGTH.getMessage());

        if(!PATTERN.matcher(document).matches()) throw new IllegalArgumentException(ErrorMessage.DOCUMENT_NOT_NUMERIC.getMessage());
    }

}
