package org.example.exception;

public class NonCyrillicCharactersException extends RuntimeException {
    public NonCyrillicCharactersException(String message) {

        super(message);
    }

}
