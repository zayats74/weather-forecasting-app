package org.example.exception.dateException;

public class InvalidDayException extends RuntimeException {
    public InvalidDayException(String message) {
        super(message);
    }
}
