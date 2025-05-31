package org.example.service.Impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.entity.WeatherDescription;
import org.example.entity.WindDescription;
import org.example.repository.WeatherDescriptionRepository;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeatherDescriptionService {

    private final WeatherDescriptionRepository weatherDescriptionRepository;
    private final CacheManager cacheManager;

    public static final String CACHE_NAME = "weatherDescription";
    public static final String CACHE_KEY = "all";

    @PostConstruct
    public void initialization(){
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            List<WeatherDescription> weatherDescriptions = weatherDescriptionRepository.findAllByOrderByMinCloudCoverAsc();
            cache.put(CACHE_KEY, weatherDescriptions);
        }
    }

    @Cacheable(value = CACHE_NAME, key = CACHE_KEY, cacheManager = "inMemoryCacheManager")
    public List<WeatherDescription> getAllWeatherDescriptions() {
        return weatherDescriptionRepository.findAllByOrderByMinCloudCoverAsc();
    }

    public Optional<WeatherDescription> findByCloudCover(double cloudCover) {
        return getAllWeatherDescriptions().stream()
                .filter(d -> isInRange(cloudCover, d.getMinCloudCover(), d.getMaxCloudCover()))
                .findFirst();
    }

    private boolean isInRange(double degree, double startDegree, double endDegree){
        return degree >= startDegree && degree <= endDegree;
    }
}
