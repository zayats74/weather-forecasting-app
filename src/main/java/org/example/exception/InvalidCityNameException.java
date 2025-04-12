package org.example.exception;

public class InvalidCityNameException extends RuntimeException {

    public InvalidCityNameException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "There is no city with that name.";
    }
}
