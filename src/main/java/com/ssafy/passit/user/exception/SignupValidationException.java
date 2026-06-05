package com.ssafy.passit.user.exception;

public class SignupValidationException extends RuntimeException {
    private final String errorField;

    public SignupValidationException(String message, String errorField) {
        super(message);
        this.errorField = errorField;
    }

    public String getErrorField() {
        return errorField;
    }
}
