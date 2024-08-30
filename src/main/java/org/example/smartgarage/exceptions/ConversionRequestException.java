package org.example.smartgarage.exceptions;

public class ConversionRequestException extends RuntimeException{
    public ConversionRequestException() {
        super();
    }

    public ConversionRequestException(String message) {
        super(message);
    }
}
