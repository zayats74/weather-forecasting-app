package org.example.service;

import org.example.entity.WindDescription;
import org.example.repository.WindDescriptionRepository;
import org.example.service.windServices.Impl.WindDescriptionService;
import org.junit.jupiter.api.BeforeEach;
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
public class WindDescriptionServiceTest {

    @Mock
    private WindDescriptionRepository windDescriptionRepository;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @InjectMocks
    private WindDescriptionService windDescriptionService;

    private final List<WindDescription> testDescriptions = List.of(
            new WindDescription(2L, "Северо-восток", 33.001, 78.0),
            new WindDescription(3L, "Восток", 78.001, 123.0),
            new WindDescription(4L, "Юго-восток", 123.001, 168.0),
            new WindDescription(5L, "Юг", 168.001, 213.0),
            new WindDescription(6L, "Юго-запад", 213.001, 258.0),
            new WindDescription(7L, "Запад", 258.001, 303.0),
            new WindDescription(8L, "Северо-запад", 303.001, 347.0),
            new WindDescription(1L, "Север", 347.001, 33.0)
    );

    @BeforeEach
    void setUp() {
        when(windDescriptionRepository.findAllByOrderByDegreeStartAsc())
                .thenReturn(testDescriptions);

        when(cacheManager.getCache(WindDescriptionService.CACHE_NAME))
                .thenReturn(cache);
        windDescriptionService.initialization();
    }

    @Test
    void isInRange_shouldHandleWrappingRange() {
        assertTrue(windDescriptionService.isInRange(347.001, 347.001, 33.0));
        assertTrue(windDescriptionService.isInRange(350.0, 347.001, 33.0));
        assertTrue(windDescriptionService.isInRange(0.0, 347.001, 33.0));
        assertTrue(windDescriptionService.isInRange(32.999, 347.001, 33.0));
        assertFalse(windDescriptionService.isInRange(33.001, 347.001, 33.0));
        assertFalse(windDescriptionService.isInRange(347.0, 347.001, 33.0));
    }

    @Test
    void isInRange_shouldHandleStandardRange() {
        assertTrue(windDescriptionService.isInRange(78.001, 78.001, 123.0));
        assertTrue(windDescriptionService.isInRange(100.0, 78.001, 123.0));
        assertTrue(windDescriptionService.isInRange(123.0, 78.001, 123.0));
        assertFalse(windDescriptionService.isInRange(78.000, 78.001, 123.0));
        assertFalse(windDescriptionService.isInRange(123.001, 78.001, 123.0));
    }

    @Test
    void normalizeDegrees_shouldHandleVariousValues() {
        assertEquals(0.0, windDescriptionService.normalizeDegrees(360.0));
        assertEquals(10.0, windDescriptionService.normalizeDegrees(370.0));
        assertEquals(350.0, windDescriptionService.normalizeDegrees(-10.0));
        assertEquals(280.0, windDescriptionService.normalizeDegrees(-80.0));
        assertEquals(303.001, windDescriptionService.normalizeDegrees(303.001 + 360.0));
        assertEquals(347.0, windDescriptionService.normalizeDegrees(347.0 - 720.0));
    }
}
