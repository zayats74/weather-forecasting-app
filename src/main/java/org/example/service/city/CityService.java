package org.example.service.city;

import org.example.entity.City;

public interface CityService {
    boolean isValidCity(String city);
    String getCityCoordinates(String city);
    City getCityByName(String city);
}
