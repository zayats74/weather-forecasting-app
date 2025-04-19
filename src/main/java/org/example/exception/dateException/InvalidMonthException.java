package org.example.exception.dateException;

public class InvalidMonthException extends RuntimeException {
    public InvalidMonthException(String message) {
        super(message);
    }
}
