package org.example.smartgarage.exceptions;

public class UserMismatchException extends RuntimeException{

    public UserMismatchException(String message){
        super(message);
    }
}
