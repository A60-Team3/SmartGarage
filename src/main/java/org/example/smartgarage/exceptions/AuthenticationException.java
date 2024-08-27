package org.example.smartgarage.exceptions;

public class AuthenticationException extends RuntimeException{
    public AuthenticationException() {
        super();
    }

    public AuthenticationException(String message) {
        super(message);
    }
}
