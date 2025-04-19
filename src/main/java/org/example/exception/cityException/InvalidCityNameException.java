package org.example.exception.cityException;

public class InvalidCityNameException extends RuntimeException {

    public InvalidCityNameException(String message) {
        super(message);
    }
}
