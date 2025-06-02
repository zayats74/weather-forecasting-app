package org.example.service.weather.Impl;

import lombok.RequiredArgsConstructor;
import org.example.config.WeatherForecastProperties;
import org.example.dto.WeatherApiResponseDTO;
import org.example.service.city.CityService;
import org.example.service.weather.WeatherAPIService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherAPIServiceImpl implements WeatherAPIService {

    private final CityService cityService;
    private final RestClient weatherClient;
    private final WeatherForecastProperties weatherForecastProperties;

    public static final String URI_DAY_FORECAST = "/api/weather/?lat={lat}&lon={lon}&date={date}&token={token}";
    public static final String URI_DAYS_FORECAST = "/api/weather/?lat={lat}&lon={lon}&date={dateStart},{dateEnd}&token={token}";
    public static final String COORDINATES_SEPARATOR = ", ";


    @Override
    public List<WeatherApiResponseDTO> getWeatherForecastOnDate(String city, LocalDate date) {
        String[] coordinates = cityService.getCityCoordinates(city).split(", ");
        List<WeatherApiResponseDTO> responseApi = weatherClient
                .get()
                .uri(URI_DAY_FORECAST,
                        coordinates[0], coordinates[1], date, weatherForecastProperties.getToken())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        return responseApi;
    }

    @Override
    public List<WeatherApiResponseDTO> getWeatherForecastOnDatesBetween(String city, LocalDate dateStart, LocalDate dateEnd) {
        String[] coordinates = cityService.getCityCoordinates(city).split(COORDINATES_SEPARATOR);
        List<WeatherApiResponseDTO> responseApi = weatherClient
                .get()
                .uri(URI_DAYS_FORECAST,
                        coordinates[0], coordinates[1], dateStart, dateEnd, weatherForecastProperties.getToken())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        return responseApi;
    }
}
