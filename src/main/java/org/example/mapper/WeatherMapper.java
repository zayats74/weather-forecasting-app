package org.example.mapper;

import org.example.dto.WeatherResponseDTO;
import org.example.entity.Weather;
import org.springframework.stereotype.Component;

@Component
public class WeatherMapper {
    public WeatherResponseDTO mapToResponseDTO(Weather weather) {
        return new WeatherResponseDTO(
                weather.getTemperature(),
                weather.getHumidity(),
                weather.getPressure(),
                weather.getUvIndex(),
                weather.getVisibility(),
                weather.getDescription(),
                weather.getWind(),
                weather.getPrecipation()
        );
    }

    public Weather mapToWeather(WeatherResponseDTO weatherResponseDTO) {
        return new Weather(
                weatherResponseDTO.getTemperature(),
                weatherResponseDTO.getHumidity(),
                weatherResponseDTO.getPressure(),
                weatherResponseDTO.getUvIndex(),
                weatherResponseDTO.getVisibility(),
                weatherResponseDTO.getDescription(),
                weatherResponseDTO.getWind(),
                weatherResponseDTO.getPrecipitation()
        );
    }
}
