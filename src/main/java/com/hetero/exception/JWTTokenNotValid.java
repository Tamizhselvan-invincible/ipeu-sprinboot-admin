package com.hetero.exception;

public class JWTTokenNotValid extends RuntimeException {
    public JWTTokenNotValid (String message) {
        super(message);
    }
}
