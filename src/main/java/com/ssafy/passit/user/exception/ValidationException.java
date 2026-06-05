package com.ssafy.passit.user.exception;

public class ValidationException extends RuntimeException {
    private final String errorField;

    public ValidationException(String message, String errorField) {
        super(message);
        this.errorField = errorField;
    }

    public String getErrorField() {
        return errorField;
    }
}
