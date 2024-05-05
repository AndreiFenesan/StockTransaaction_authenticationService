package com.example.authenticationservice.exception;

public class InvalidTokenType extends RuntimeException{
    public InvalidTokenType() {
    }

    public InvalidTokenType(String message) {
        super(message);
    }
}
