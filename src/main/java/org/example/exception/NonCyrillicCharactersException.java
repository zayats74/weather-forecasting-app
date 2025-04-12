package org.example.exception;

public class NonCyrillicCharactersException extends RuntimeException {
    public NonCyrillicCharactersException(String message) {

        super(message);
    }

    @Override
    public String getMessage() {
        return "The name of the city must be entered in Cyrillic.";
    }
}
