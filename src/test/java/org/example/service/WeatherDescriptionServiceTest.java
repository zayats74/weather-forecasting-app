package org.example.service;

import org.example.entity.WeatherDescription;
import org.example.repository.WeatherDescriptionRepository;
import org.example.service.weatherServices.Impl.WeatherDescriptionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WeatherDescriptionServiceTest {

    @Mock
    private WeatherDescriptionRepository weatherDescriptionRepository;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @InjectMocks
    private WeatherDescriptionService weatherDescriptionService;

    private final WeatherDescription w1 = new WeatherDescription(1L, "ясно", 0.0, 20.0);
    private final WeatherDescription w2 = new WeatherDescription(2L, "малооблачно", 20.001, 50.0);
    private final WeatherDescription w3 = new WeatherDescription(3L, "переменная облачность", 50.001, 70.0);
    private final WeatherDescription w4 = new WeatherDescription(4L, "облачно с прояснениями", 70.001, 80.0);
    private final WeatherDescription w5 = new WeatherDescription(5L, "преимущественно облачно", 80.001, 90.0);
    private final WeatherDescription w6 = new WeatherDescription(6L, "пасмурно", 90.001, 100.0);
    private final List<WeatherDescription> allDescriptions = List.of(w1, w2, w3, w4, w5, w6);

    @Test
    void initialization_ShouldLoadDataIntoCache() {
        when(cacheManager.getCache(WeatherDescriptionService.CACHE_NAME)).thenReturn(cache);
        when(weatherDescriptionRepository.findAllByOrderByMinCloudCoverAsc()).thenReturn(allDescriptions);

        weatherDescriptionService.initialization();

        verify(cache).put(WeatherDescriptionService.CACHE_KEY, allDescriptions);
    }


    @Test
    void isInRange_ShouldCorrectlyCheckBoundaries() {
        assertTrue(weatherDescriptionService.isInRange(10.0, 0.0, 20.0));
        assertTrue(weatherDescriptionService.isInRange(0.0, 0.0, 20.0));
        assertTrue(weatherDescriptionService.isInRange(20.0, 0.0, 20.0));

        double epsilon = WeatherDescriptionService.EPSILON;
        assertTrue(weatherDescriptionService.isInRange(20.0 + epsilon, 0.0, 20.0));
        assertFalse(weatherDescriptionService.isInRange(20.0 + epsilon*2, 0.0, 20.0));
    }
}
