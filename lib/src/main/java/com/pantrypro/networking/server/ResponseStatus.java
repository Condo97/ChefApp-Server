package com.pantrypro.networking.server;

public enum ResponseStatus {
    SUCCESS(1),

    JSON_ERROR(4),
    CAP_REACHED_ERROR(51),

    // Errors with services
    OAIGPT_ERROR(60),
    INVALID_APPLE_TRANSACTION_ERROR(61),

    // PantryPro error
    INVALID_ASSOCIATED_ID(70),
    GENERATION_ERROR(71),
    ILLEGAL_ARGUMENT(80),
    UNHANDLED_ERROR(99);

    public final int Success;

    ResponseStatus(int Success) { this.Success = Success; }
}
