package org.example.service.windServices.Impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.WeatherApiResponseDTO;
import org.example.entity.Schedule;
import org.example.entity.Wind;
import org.example.entity.WindDescription;
import org.example.mapper.WindMapper;
import org.example.repository.WindRepository;
import org.example.service.windServices.WindService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WindServiceImpl implements WindService {

    private final WindRepository windRepository;
    private final WindMapper windMapper;
    private final WindDescriptionService windDescriptionService;

    @Override
    public Wind createOrUpdateWind(WeatherApiResponseDTO weatherApiResponseDTO, Schedule schedule) {
        WindDescription windDesc = getWindDescription(weatherApiResponseDTO.getWindDirection());
        Wind wind = windRepository.findByScheduleId(schedule.getId());

        if (wind != null) {
            wind.setSpeed(weatherApiResponseDTO.getWindSpeed());
            wind.setWindDescription(windDesc);
        }
        else {
            wind = windMapper.mapToWind(weatherApiResponseDTO);
            wind.setSchedule(schedule);
            wind.setWindDescription(windDesc);
        }

        return windRepository.save(wind);
    }

    @Override
    public WindDescription getWindDescription(double degrees) {
        final double normalized = windDescriptionService.normalizeDegrees(degrees);
        return windDescriptionService.getAllWindDescriptions().stream()
                .filter(d -> windDescriptionService.isInRange(normalized, d.getDegreeStart(), d.getDegreeEnd()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("WindDescription not found"));
    }

    @Override
    public Wind getWindByScheduleId(Long id){
        return windRepository.findByScheduleId(id);
    }
}
