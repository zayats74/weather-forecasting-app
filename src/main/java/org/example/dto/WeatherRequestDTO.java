package org.example.dto;

import java.time.LocalDate;

public class WeatherRequestDTO {
    private String city;

    private LocalDate date;


    //Getters
    public String getCity() {
        return city;
    }

    public LocalDate getDate() {
        return date;
    }


    //Setters
    public void setCity(String city) {
        this.city = city;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    //Constructors
    public WeatherRequestDTO() {}

    public WeatherRequestDTO(String city, LocalDate date) {
        this.city = city;
        this.date = date;
    }
}
