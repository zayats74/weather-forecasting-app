package org.example.service;

import org.example.dto.WeatherResponseDTO;
import org.example.entity.Weather;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Random;

public interface WeatherService {
    WeatherResponseDTO getWeatherForecast(Random rand);
    WeatherResponseDTO getWeatherForecastOnToday(String city);
    WeatherResponseDTO getWeatherForecastOnTomorrow(String city);
    HashMap<LocalDate, WeatherResponseDTO> getWeatherForecastOnTenDays(String city);
    WeatherResponseDTO getWeatherForecastOnSetDay(String city, LocalDate date);
}
