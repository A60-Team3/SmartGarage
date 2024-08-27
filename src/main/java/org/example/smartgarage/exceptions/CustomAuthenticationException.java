package org.example.smartgarage.exceptions;

public class CustomAuthenticationException extends RuntimeException{
    public CustomAuthenticationException(String message) {
        super(message);
    }
}
