package org.example.mapper;

import org.example.dto.WeatherApiResponseDTO;
import org.example.dto.WeatherResponseDTO;
import org.example.entity.Weather;
import org.example.entity.Wind;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WeatherMapper {

    @Mapping(target = "scheduleId", ignore = true)
    @Mapping(target = "schedule", ignore = true)
    Weather mapToWeather(WeatherApiResponseDTO weatherApiResponseDTO);

    @Mapping(source = "weather.weatherDescription", target = "description")
    WeatherResponseDTO mapToResponseDTO(Weather weather, Wind wind);
}
