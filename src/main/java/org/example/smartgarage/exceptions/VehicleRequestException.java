package org.example.smartgarage.exceptions;

public class VehicleRequestException extends RuntimeException{
    public VehicleRequestException() {
        super();
    }

    public VehicleRequestException(String message) {
        super(message);
    }
}
