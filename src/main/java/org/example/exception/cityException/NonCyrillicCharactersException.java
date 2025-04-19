package org.example.exception.cityException;

public class NonCyrillicCharactersException extends RuntimeException {
    public NonCyrillicCharactersException(String message) {

        super(message);
    }

}
