package org.example.service.weatherServices.Impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.WeatherResponseDTO;
import org.example.service.weatherServices.WeatherForecastService;
import org.example.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeatherForecastServiceImpl implements WeatherForecastService {

    private final WeatherService weatherService;

    @Override
    public List<WeatherResponseDTO> getWeatherForecast(String city, LocalDate date) {
        if (!date.isBefore(LocalDate.now()) && !date.isAfter(LocalDate.now().plusDays(9))) {
            return weatherService.getWeatherForecastOnDays(city, 10)
                    .stream().filter(dto -> date.equals(dto.getDate()))
                    .collect(Collectors.toList());
        }
        else {
            return weatherService.getWeatherForecastOnDay(city, date);
        }
    }

    @Override
    public List<WeatherResponseDTO> getWeatherForecastOnToday(String city) {
        LocalDate date = DateUtils.getCurrentDate();
        return getWeatherForecast(city, date);
    }

    @Override
    public List<WeatherResponseDTO> getWeatherForecastOnTomorrow(String city) {
        LocalDate date = DateUtils.getTomorrowDate();
        return getWeatherForecast(city, date);
    }

    @Override
    public List<WeatherResponseDTO> getWeatherForecastOnTenDays(String city) {
        return weatherService.getWeatherForecastOnDays(city, 10);
    }

    @Override
    public List<WeatherResponseDTO> getWeatherForecastOnSetDay(String city, LocalDate date) {
        return getWeatherForecast(city, date);
    }



}
