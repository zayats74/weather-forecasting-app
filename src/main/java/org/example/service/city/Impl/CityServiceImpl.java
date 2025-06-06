package org.example.service.city.Impl;

import org.example.config.CityCoordinatesProperties;
import org.example.dto.CityResponseDTO;
import org.example.entity.City;
import org.example.exception.cityException.InvalidCityFormatException;
import org.example.exception.cityException.InvalidCityNameException;
import org.example.exception.cityException.NonCyrillicCharactersException;
import org.example.repository.CityRepository;
import org.example.service.city.CityService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final RestClient restClient;
    private final CityCoordinatesProperties cityCoordinatesProperties;

    public static final String CYRILLIC = "^[А-Яа-яЁё]+(?:[-\\s][А-Яа-яЁё]+)*$";
    public static final String URI_CITY_COORDINATES = "/v1?apikey={apikey}&geocode={city}&format=json";


    public CityServiceImpl(CityRepository cityRepository, @Qualifier("сityClient") RestClient restClient, CityCoordinatesProperties cityCoordinatesProperties) {
        this.cityRepository = cityRepository;
        this.restClient = restClient;
        this.cityCoordinatesProperties = cityCoordinatesProperties;
    }

    private boolean isCyrillic(String str){
        return str.matches(CYRILLIC);
    }

    @Override
    public boolean isValidCity(String city) {
        if (!isCyrillic(city)) {
            throw new NonCyrillicCharactersException("Название города должно быть введено кириллицей.");
        } else if ('А' > city.charAt(0) || city.charAt(0) > 'Я') {
            throw new InvalidCityFormatException("Название города должно начинаться с заглавной буквы");
        } else if (!cityRepository.existsByCity(city)) {
            throw new InvalidCityNameException("Города с таким названием в России не существует.");
        }
        return true;
    }

    public String getCityCoordinates(String city) {
        CityResponseDTO response = restClient
                .get()
                .uri(URI_CITY_COORDINATES, cityCoordinatesProperties.getApiKey(), city)
                .retrieve()
                .body(CityResponseDTO.class);
        double[] coordinates = response.getCoordinates();
        return coordinates[0] + ", " + coordinates[1];
    }

    @Override
    public City getCityByName(String city){
        return cityRepository.findByCity(city);
    }

}
