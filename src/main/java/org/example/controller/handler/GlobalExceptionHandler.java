package org.example.controller.handler;

import org.example.exception.cityException.InvalidCityFormatException;
import org.example.exception.cityException.InvalidCityNameException;
import org.example.exception.cityException.NonCyrillicCharactersException;
import org.example.exception.dateException.*;
import org.example.service.cityServices.Impl.CityServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NonCyrillicCharactersException.class,
                        InvalidCityFormatException.class,
                        InvalidCityNameException.class})
    public ResponseEntity<String> handleCityException(RuntimeException  e){
        Logger.getLogger(CityServiceImpl.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler({NonNumericDateException.class,
                       InvalidDateFormatException.class,
                       InvalidYearException.class,
                       InvalidMonthException.class,
                       InvalidDayException.class})
    public ResponseEntity<String> handleDataException(RuntimeException e){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

}
