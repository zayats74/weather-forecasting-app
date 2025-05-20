package org.example.mapper;

import org.example.dto.WeatherApiResponseDTO;
import org.example.entity.Wind;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WindMapper {

    @Mapping(target = "scheduleId", ignore = true)
    @Mapping(target = "schedule", ignore = true)
    @Mapping(target = "speed", source = "windSpeed")
    Wind mapToWind(WeatherApiResponseDTO weatherApiResponseDTO);

}
