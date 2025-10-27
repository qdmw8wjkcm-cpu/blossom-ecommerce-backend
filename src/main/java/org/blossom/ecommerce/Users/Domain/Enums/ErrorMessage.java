package org.blossom.ecommerce.Users.Domain.Enums;

public enum ErrorMessage {

    //Document errors
    DOCUMENT_BLANK("Document cannot be blank"),
    DOCUMENT_INVALID_LENGTH("Document must be between 5 and 10 digits"),
    DOCUMENT_NOT_NUMERIC("Document must contain only numeric characters"),

    // Email errors
    EMAIL_NULL("Email cannot be null"),
    EMAIL_BLANK("Email cannot be blank"),
    EMAIL_INVALID("The email format is incorrect"),
    EMAIL_IN_USE("Email already in use"),

    //Password
    PASSWORD_NULL("Hashed password cannot be null"),
    PASSWORD_BLANK("Hashed password cannot be blank"),
    PASSWORD_FORMATT("Hashed must have exactly 60 characters"),

    //PhoneNumber
    PHONE_NUMBER_NULL("Phone number cannot be null"),
    PHONE_NUMBER_BLANK("Phone number cannot be blank"),
    PHONE_NUMBER_FORMATT("Phone number have exactly 60 characters"),

    //User
    USER_NULL("Hashed password cannot be null"),
    USER_NOT_FOUND("User not found"),
    USER_EXIST("User exist"),
    EMAIL_IS_THE_SAME("Email must be different" ),

    //Product
    PRODUCT_NULL("Product cannot be null"),
    PRODUCT_BLANK("Product cannot be blank"),
    PRODUCT_EXIST("Product exist in favorite"),
    PRODUCT_NOT_EXIST("Product not exist in favorite"),

    //Order
    ORDER_NULL("Order cannot be null"),
    ORDER_ZERO("Order cannot be zero UUID"),

    //Category
    CATEGORY_NULL("Category cannot be null"),
    CATEGORY_ZERO("Category cannot be zero UUID"),
    CATEGORY_EXIST("Category exist in favorite"),
    CATEGORY_NOT_EXIST("Category not exist in favorite"),

    //Phone number
    PHONE_BLANK("Phone number cannot be null or empty"),
    PHONE_INVALID("Phone number format is invalid: "),

    //Credentials
    CREDENTIAL_INVALID("Password is invalid"),

    //Status
    STATUS_IS_THE_SAME("The status must be different"),
    STATUS_NOT_FOUND("The status dont exist"),
    STATUS_INVALID("Invalid status change request"),
    ;

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}