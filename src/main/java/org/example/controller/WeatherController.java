package org.example.controller;

import org.example.dto.WeatherResponseDTO;
import org.example.service.CityService;
import org.example.service.WeatherService;
import org.example.utils.DateUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@RestController
@RequestMapping("api/v1/weather-forecast")
public class WeatherController {

    private final WeatherService weatherService;

    private final CityService cityService;

    public WeatherController(WeatherService weatherService, CityService cityService) {
        this.weatherService = weatherService;
        this.cityService = cityService;
    }

    @GetMapping("/{city}/today")
    public WeatherResponseDTO getWeatherOnToday(@PathVariable String city) {
        cityService.isValidCity(city);
        return weatherService.getWeatherForecastOnToday(city);
    }

    @GetMapping("/{city}/tomorrow")
    public WeatherResponseDTO getWeatherOnTomorrow(@PathVariable String city) {
        cityService.isValidCity(city);
        return weatherService.getWeatherForecastOnTomorrow(city);
    }

    @GetMapping("/{city}/{date}")
    public WeatherResponseDTO getWeatherOnDate(@PathVariable String city,
                                               @PathVariable String date) {
        cityService.isValidCity(city);
        DateUtils.isValidDate(date);
        return weatherService.getWeatherForecastOnSetDay(city,
                LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    @GetMapping("/{city}")
    public HashMap<LocalDate, WeatherResponseDTO> getWeatherForecast(@PathVariable String city) {
        cityService.isValidCity(city);
        return weatherService.getWeatherForecastOnTenDays(city);
    }
}
