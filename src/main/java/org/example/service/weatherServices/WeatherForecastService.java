package org.example.service.weatherServices;

import org.example.dto.WeatherResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface WeatherForecastService {
    List<WeatherResponseDTO> getWeatherForecast(String city, LocalDate date);
    List<WeatherResponseDTO> getWeatherForecastOnToday(String city);
    List<WeatherResponseDTO> getWeatherForecastOnTomorrow(String city);
    List<WeatherResponseDTO> getWeatherForecastOnTenDays(String city);
    List<WeatherResponseDTO> getWeatherForecastOnSetDay(String city, LocalDate date);
}
