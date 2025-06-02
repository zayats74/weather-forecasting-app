package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.dto.WeatherResponseDTO;
import org.example.service.city.CityService;
import org.example.service.weather.WeatherForecastService;
import org.example.utils.DateUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Tag(name = "Контроллер прогнозов погоды", description = "Позволяет получать прогнозы погоды")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/weather-forecast")
public class WeatherController {

    private final WeatherForecastService weatherForecastService;

    private final CityService cityService;

    @Operation(summary = "Получение прогноза погода на сеодняшний день",
        description = "Позволяет получить прогноз погоды в заданном городе на сегодня")
    @GetMapping("/{city}/today")
    public List<WeatherResponseDTO> getWeatherOnToday(@PathVariable
                                                          @Parameter(name = "Название города", required = true) String city) {
        cityService.isValidCity(city);
        return weatherForecastService.getWeatherForecastOnToday(city);
    }

    @Operation(summary = "Получение прогноза погода на завтрашний день",
            description = "Позволяет получить прогноз погоды в заданном городе на завтра")
    @GetMapping("/{city}/tomorrow")
    public List<WeatherResponseDTO> getWeatherOnTomorrow(@PathVariable
                                                             @Parameter(name = "Название города", required = true) String city) {
        cityService.isValidCity(city);
        return weatherForecastService.getWeatherForecastOnTomorrow(city);
    }

    @Operation(summary = "Получение прогноза погода в заданный день",
            description = "Позволяет получить прогноз погоды в заданном городе в заданный день")
    @GetMapping("/{city}/{date}")
    public List<WeatherResponseDTO> getWeatherOnDate(@PathVariable
                                                         @Parameter(name = "Название города", required = true) String city,
                                               @PathVariable
                                               @Parameter(name = "Дата (ДД.ММ.ГГГГ)", required = true) String date) {
        cityService.isValidCity(city);
        DateUtils.isValidDate(date);
        return weatherForecastService.getWeatherForecastOnSetDay(city,
                LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    @Operation(summary = "Получение прогноза погода на 10 дней вперед",
            description = "Позволяет получить прогноз погоды в заданном городе на 10 дней")
    @GetMapping("/{city}")
    public List<WeatherResponseDTO> getWeatherForecast(@PathVariable
                                                           @Parameter(name = "Название города", required = true) String city) {
        cityService.isValidCity(city);
        return weatherForecastService.getWeatherForecastOnTenDays(city);
    }
}
