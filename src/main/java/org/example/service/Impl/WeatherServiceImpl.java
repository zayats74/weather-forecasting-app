package org.example.service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.WeatherResponseDTO;
import org.example.repository.*;
import org.example.service.*;
import org.example.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final CityRepository cityRepository;
    private final WeatherCacheService weatherCacheService;
    private final WeatherForecastDataService weatherForecastDataService;


    @Override
    public List<WeatherResponseDTO> getWeatherForecastOnDay(String city, LocalDate date) {
        if (!date.isBefore(LocalDate.now()) && !date.isAfter(LocalDate.now().plusDays(9))) {
            return weatherCacheService.getCachedWeatherForecastOnTenDays(city)
                    .stream().filter(dto -> date.equals(dto.getDate()))
                    .collect(Collectors.toList());
        }
        else {
            if (weatherForecastDataService.checkDay(cityRepository.findByCity(city), date)) {
                return weatherForecastDataService.getExistingForeCast(cityRepository.findByCity(city), date);
            }
            else {
               return weatherForecastDataService.getWeatherForecastOnDate(city, date);
            }
        }
    }

    @Override
    public List<WeatherResponseDTO> getWeatherForecastOnToday(String city) {
        LocalDate date = DateUtils.getCurrentDate();
        return getWeatherForecastOnDay(city, date);
    }

    @Override
    public List<WeatherResponseDTO> getWeatherForecastOnTomorrow(String city) {
        LocalDate date = DateUtils.getTomorrowDate();
        return getWeatherForecastOnDay(city, date);
    }

    @Override
    public List<WeatherResponseDTO> getWeatherForecastOnTenDays(String city) {
        LocalDate dateStart = LocalDate.now();
        LocalDate dateEnd = dateStart.plusDays(9);
        if (weatherForecastDataService.isFullForecast(city, dateStart, dateEnd)){
            return weatherForecastDataService.getExistingForeCastInRange(city, dateStart, dateEnd);
        }
        else {
            return weatherCacheService.getCachedWeatherForecastOnTenDays(city);
        }
    }

    @Override
    public List<WeatherResponseDTO> getWeatherForecastOnSetDay(String city, LocalDate date) {
        return getWeatherForecastOnDay(city, date);
    }



}
