package org.example.service;

import org.example.exception.cityException.InvalidCityFormatException;
import org.example.exception.cityException.InvalidCityNameException;
import org.example.exception.cityException.NonCyrillicCharactersException;
import org.example.repository.CityRepository;
import org.example.service.city.Impl.CityServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CityServiceImplTest {
    @Mock
    private CityRepository cityRepository;

    @Mock
    private RestClient restClient;

    @InjectMocks
    private CityServiceImpl cityService;

    @Test
    void isValidCity_ShouldReturnTrueForValidCity() {
        String cityName = "Москва";
        when(cityRepository.existsByCity(cityName)).thenReturn(true);

        assertTrue(cityService.isValidCity(cityName));
        verify(cityRepository).existsByCity(cityName);
    }

    @Test
    void isValidCity_ShouldThowNonCyrillicException() {
        String cityName = "Mocsow";

        assertThrows(NonCyrillicCharactersException.class,
                () -> cityService.isValidCity(cityName));
    }

    @Test
    void isValidCity_ShouldThrowInvalidFormatException() {
        String cityName = "москва";

        assertThrows(InvalidCityFormatException.class,
                () -> cityService.isValidCity(cityName));
    }

    @Test
    void isValidCity_ShouldThrowInvalidNameException() {
        String cityName = "Город";
        when(cityRepository.existsByCity(cityName)).thenReturn(false);

        assertThrows(InvalidCityNameException.class,
                () -> cityService.isValidCity(cityName));
    }

}
