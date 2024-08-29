package org.example.smartgarage.exceptions;

public class InvalidFilterArgumentException extends RuntimeException{
    public InvalidFilterArgumentException() {
    }

    public InvalidFilterArgumentException(String message) {
        super(message);
    }
}
