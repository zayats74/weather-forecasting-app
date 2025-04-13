package org.example.exception;

public class InvalidCityFormatException extends RuntimeException {
    public InvalidCityFormatException(String message) {
      super(message);
    }

}
