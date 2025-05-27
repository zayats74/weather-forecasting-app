package org.example.service;

import org.example.entity.WeatherDescription;
import org.example.repository.WeatherDescriptionRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WeatherDescriptionService {

    private final WeatherDescriptionRepository weatherDescriptionRepository;

    public static final String CACHE_NAME = "weatherDescription";

    public WeatherDescriptionService(WeatherDescriptionRepository weatherDescriptionRepository) {
        this.weatherDescriptionRepository = weatherDescriptionRepository;
    }

    @Cacheable(CACHE_NAME)
    public List<WeatherDescription> getAllWeatherDescriptions() {
        return weatherDescriptionRepository.findAllByOrderByMinCloudCoverAsc();
    }

    public Optional<WeatherDescription> findByCloudCover(double cloudCover) {
        return getAllWeatherDescriptions().stream()
                .filter(d -> isInRange(cloudCover, d.getMinCloudCover(), d.getMaxCloudCover()))
                .findFirst();
    }

    private boolean isInRange(double degree, double startDegree, double endDegree){
        return degree >= startDegree && degree < endDegree;
    }
}
