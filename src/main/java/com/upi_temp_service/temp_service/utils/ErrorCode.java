package com.upi_temp_service.temp_service.utils;

public enum ErrorCode {
    SUCCESS(200, "Request submitted for approval"),
    TOKEN_INVALID(1000, "Token validation failed"),
    DATA_NOT_FOUND(404, "Requested data not found"),
    VALIDATION_ERROR(400, "Invalid input data"),
    INTERNAL_ERROR(500, "Internal server error"),
    UNAUTHORIZED(403, "You are not authorized to access this resource"),
    DECRYPTION_FAILED(501,"Decryption Failed");

    private final int code;
    private final String defaultMessage;

    ErrorCode(int code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public int getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
