package org.example.service.Impl;

import org.example.exception.cityException.InvalidCityFormatException;
import org.example.exception.cityException.InvalidCityNameException;
import org.example.exception.cityException.NonCyrillicCharactersException;
import org.example.repository.CityRepository;
import org.example.service.CityService;
import org.springframework.stereotype.Service;


@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    private boolean isCyrillic(String str){
        return str.matches("[а-яА-Я]+");
    }

    @Override
    public boolean isValidCity(String city) {
        if (!isCyrillic(city)) {
            throw new NonCyrillicCharactersException("Название города должно быть введено кириллицей.");
        } else if ('А' > city.charAt(0) || city.charAt(0) > 'Я') {
            throw new InvalidCityFormatException("Название города должно начинаться с заглавной буквы");
        } else if (!cityRepository.containsCity(city)) {
            throw new InvalidCityNameException("Города с таким названием в России не существует.");
        }
        return true;
    }

}
