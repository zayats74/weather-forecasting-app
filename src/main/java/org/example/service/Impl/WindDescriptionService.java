package org.example.service.Impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.entity.WindDescription;
import org.example.repository.WindDescriptionRepository;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WindDescriptionService {

    private final WindDescriptionRepository windDescriptionRepository;
    private final CacheManager cacheManager;

    public static final String CACHE_NAME = "windDescription";
    public static final String CACHE_KEY = "all";
    public static final Double EPSILON = 0.0001;

    @PostConstruct
    public void initialization(){
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            List<WindDescription> windDescriptions = windDescriptionRepository.findAllByOrderByDegreeStartAsc();
            cache.put(CACHE_KEY, windDescriptions);
        }
    }


    @Cacheable(value = CACHE_NAME, key = CACHE_KEY, cacheManager = "inMemoryCacheManager")
    public List<WindDescription> getAllWindDescriptions() {
        return windDescriptionRepository.findAllByOrderByDegreeStartAsc();
    }

    public Optional<WindDescription> findByDegree(double degrees) {
        final double normalized = normalizeDegrees(degrees);
        return getAllWindDescriptions().stream()
                .filter(d -> isInRange(normalized, d.getDegreeStart(), d.getDegreeEnd()))
                .findFirst();
    }

    public boolean isInRange(double degree, double startDegree, double endDegree){
        if (startDegree > endDegree) {
            return degree >= startDegree - EPSILON || degree <= endDegree + EPSILON;
        }
        return degree >= startDegree - EPSILON && degree <= endDegree + EPSILON;
    }

    public double normalizeDegrees(double degrees) {
        return (degrees % 360 + 360) % 360;
    }
}
