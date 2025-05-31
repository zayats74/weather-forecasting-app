package org.example.service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.WeatherResponseDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherCacheService {
    private final WeatherForecastDataService weatherForecastDataService;

    public static final String CACHE_NAME = "weatherForecast";
    public static final String CACHE_KEY = "#city";


    @Cacheable(cacheNames = CACHE_NAME, key = CACHE_KEY, cacheManager = "cacheManager")
    public List<WeatherResponseDTO> getCachedWeatherForecastOnTenDays(String city) {
        LocalDate dateStart = LocalDate.now();
        LocalDate dateEnd = LocalDate.now().plusDays(9);

        if (weatherForecastDataService.isFullForecast(city, dateStart, dateEnd)) {
            return weatherForecastDataService.getExistingForeCastInRange(city, dateStart, dateEnd);
        }
        return weatherForecastDataService.getWeatherForecastOnDatesBetween(city, dateStart, dateEnd);
    }
}
