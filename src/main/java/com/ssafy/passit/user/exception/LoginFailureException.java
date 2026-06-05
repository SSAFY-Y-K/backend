package com.ssafy.passit.user.exception;

public class LoginFailureException extends RuntimeException {

    public LoginFailureException(String message) {
        super(message);
    }
}
