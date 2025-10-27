package org.blossom.ecommerce.Orders.Domain.Enums;

public enum OrderMessage {

     USER_REQUIRED("User is required to create an order."),
    ITEMS_REQUIRED("The order must contain at least one product."),
    PAYMENT_REQUIRED("Payment method is required."),

    INVALID_DISCOUNT("Invalid discount value."),
    DISCOUNT_EXCEEDS_TOTAL("Discount cannot exceed the total amount."),

    INVALID_STATE_TRANSITION("Invalid state transition for this order."),
    CANNOT_CANCEL_DELIVERED("Delivered orders cannot be cancelled."),
    INVALID_REFUND("Only delivered orders can be refunded."),

    INVALID_ITEM_QUANTITY("Product quantity must be greater than zero."),
    INVALID_ITEM_PRICE("Product price must be greater than zero."),
    ITEM_NOT_FOUND("The specified product was not found in the order."),

    ORDER_NOT_FOUND("Order not found."),
    ORDER_NULL("Order cant be null."),
    INTERNAL_ERROR("An internal error occurred while processing the order.");

    private final String message;

    OrderMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}