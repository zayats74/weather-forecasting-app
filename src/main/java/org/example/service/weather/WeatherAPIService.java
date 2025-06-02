package org.example.service.weather;

import org.example.dto.WeatherApiResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface WeatherAPIService {
    List<WeatherApiResponseDTO> getWeatherForecastOnDate(String city, LocalDate date);

    List<WeatherApiResponseDTO> getWeatherForecastOnDatesBetween(String city, LocalDate dateStart, LocalDate dateEnd);
}
