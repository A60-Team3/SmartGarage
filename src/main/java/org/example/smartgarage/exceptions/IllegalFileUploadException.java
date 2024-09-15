package org.example.smartgarage.exceptions;

public class IllegalFileUploadException extends RuntimeException{
    public IllegalFileUploadException(String message) {
        super(message);
    }
}
