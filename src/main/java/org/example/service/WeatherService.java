package org.example.service;

import org.example.entity.Weather;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Random;

public interface WeatherService {
    Weather getWeatherForecast(Random rand);
    Weather getWeatherForecastOnToday(String city);
    Weather getWeatherForecastOnTomorrow(String city);
    HashMap<LocalDate, Weather> getWeatherForecastOnTenDays(String city);
    Weather getWeatherForecastOnSetDay(String city, LocalDate date);
}
