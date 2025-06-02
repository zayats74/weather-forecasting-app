package org.example.service.windServices;

import org.example.dto.WeatherApiResponseDTO;
import org.example.entity.Schedule;
import org.example.entity.Wind;
import org.example.entity.WindDescription;

public interface WindService {
    Wind createOrUpdateWind(WeatherApiResponseDTO weatherApiResponseDTO, Schedule schedule);

    WindDescription getWindDescription(double degrees);

    Wind getWindByScheduleId(Long id);
}
