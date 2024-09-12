package org.example.smartgarage.exceptions;

public class ReportCreationException extends RuntimeException{
    public ReportCreationException() {
    }

    public ReportCreationException(String message) {
        super(message);
    }
}
