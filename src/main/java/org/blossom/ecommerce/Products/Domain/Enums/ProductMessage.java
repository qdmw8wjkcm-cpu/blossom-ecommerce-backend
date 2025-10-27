package org.blossom.ecommerce.Products.Domain.Enums;

public enum ProductMessage {

    // --------- CREATION ---------
    PRODUCT_CREATED_SUCCESSFULLY("Product created successfully."),
    PRODUCT_UPDATED_SUCCESSFULLY("Product updated successfully."),
    PRODUCT_DELETED_SUCCESSFULLY("Product deleted successfully."),
    PRODUCT_NOT_FOUND("Product not found."),
    PRODUCT_ALREADY_EXISTS("A product with this title or ID already exists."),

    // --------- VALIDATIONS ---------
    INVALID_PRICE("Price must be greater than or equal to zero."),
    INVALID_STOCK("Stock must be greater than or equal to zero."),
    INVALID_DISCOUNT_PERCENTAGE("Discount percentage must be between 0 and 100."),
    INVALID_DISCOUNT_AMOUNT("Discount amount must be greater than or equal to zero."),
    INVALID_DISCOUNT_DATES("Discount end date must be after start date."),
    INVALID_RATING("Rating must be between 0.0 and 5.0."),
    EMPTY_TITLE("Product title cannot be empty."),
    EMPTY_CATEGORY("Product category cannot be empty."),
    EMPTY_DESCRIPTION("Product description cannot be empty."),

    // --------- DOMAIN ACTIONS ---------
    DISCOUNT_APPLIED("Discount applied successfully."),
    DISCOUNT_REMOVED("Discount removed successfully."),
    STOCK_UPDATED("Stock updated successfully."),
    STOCK_INSUFFICIENT("Not enough stock available."),
    COMMENT_ADDED("Comment added successfully."),
    EMPTY_COMMENT("Comment cannot be empty."),
    PRODUCT_RESTORED("Product restored successfully."),
    PRODUCT_BLOCKED("Product temporarily disabled or out of stock."),

    // --------- SYSTEM / DATABASE ---------
    DATABASE_ERROR("Unexpected error while saving the product."),
    CONSTRAINT_VIOLATION("Constraint violation while saving the product."),
    LAZY_INITIALIZATION_ERROR("Lazy initialization failed — missing transactional context."),
    UNKNOWN_ERROR("An unexpected error occurred.");

    private final String message;

    ProductMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}