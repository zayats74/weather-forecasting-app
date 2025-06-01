package org.example.service;

import org.example.entity.WeatherDescription;
import org.example.repository.WeatherDescriptionRepository;
import org.example.service.Impl.WeatherDescriptionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.List;
import java.util.Optional;

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
    void findByCloudCover_ShouldReturnCorrectDescription() {
        when(weatherDescriptionRepository.findAllByOrderByMinCloudCoverAsc()).thenReturn(allDescriptions);

        assertEquals(Optional.of(w1), weatherDescriptionService.findByCloudCover(0.0));
        assertEquals(Optional.of(w1), weatherDescriptionService.findByCloudCover(10.0));
        assertEquals(Optional.of(w1), weatherDescriptionService.findByCloudCover(20.0));

        assertEquals(Optional.of(w2), weatherDescriptionService.findByCloudCover(20.001));
        assertEquals(Optional.of(w2), weatherDescriptionService.findByCloudCover(35.0));
        assertEquals(Optional.of(w2), weatherDescriptionService.findByCloudCover(50.0));

        assertEquals(Optional.of(w3), weatherDescriptionService.findByCloudCover(50.001));
        assertEquals(Optional.of(w3), weatherDescriptionService.findByCloudCover(60.0));
        assertEquals(Optional.of(w3), weatherDescriptionService.findByCloudCover(70.0));

        assertEquals(Optional.of(w4), weatherDescriptionService.findByCloudCover(70.001));
        assertEquals(Optional.of(w4), weatherDescriptionService.findByCloudCover(75.0));
        assertEquals(Optional.of(w4), weatherDescriptionService.findByCloudCover(80.0));

        assertEquals(Optional.of(w5), weatherDescriptionService.findByCloudCover(80.001));
        assertEquals(Optional.of(w5), weatherDescriptionService.findByCloudCover(85.0));
        assertEquals(Optional.of(w5), weatherDescriptionService.findByCloudCover(90.0));

        assertEquals(Optional.of(w6), weatherDescriptionService.findByCloudCover(90.001));
        assertEquals(Optional.of(w6), weatherDescriptionService.findByCloudCover(95.0));
        assertEquals(Optional.of(w6), weatherDescriptionService.findByCloudCover(100.0));

        assertTrue(weatherDescriptionService.findByCloudCover(-0.1).isEmpty());
        assertTrue(weatherDescriptionService.findByCloudCover(100.1).isEmpty());
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
