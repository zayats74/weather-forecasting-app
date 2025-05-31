package org.example.service.Impl;

import org.example.entity.WindDescription;
import org.example.repository.WindDescriptionRepository;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WindDescriptionService {

    private final WindDescriptionRepository windDescriptionRepository;

    public static final String CACHE_NAME = "windDescription";

    public WindDescriptionService(WindDescriptionRepository windDescriptionRepository, CacheManager cacheManager) {
        this.windDescriptionRepository = windDescriptionRepository;
    }

    @Cacheable(value = CACHE_NAME, cacheManager = "inMemoryCacheManager")
    public List<WindDescription> getAllWindDescriptions() {
        return windDescriptionRepository.findAllByOrderByDegreeStartAsc();
    }

    public Optional<WindDescription> findByDegree(double degrees) {
        final double normalized = normalizeDegrees(degrees);
        return getAllWindDescriptions().stream()
                .filter(d -> isInRange(normalized, d.getDegreeStart(), d.getDegreeEnd()))
                .findFirst();
    }

    private boolean isInRange(double degree, double startDegree, double endDegree){
        if (startDegree > endDegree) {
            return degree >= startDegree || degree <= endDegree;
        }
        return degree >= startDegree && degree <= endDegree;
    }

    private double normalizeDegrees(double degrees) {
        return (degrees % 360 + 360) % 360;
    }
}
