package org.example.service.Impl;

import org.example.dto.WeatherResponseDTO;
import org.example.entity.Weather;
import org.example.mapper.WeatherMapper;
import org.example.repository.WeatherRepository;
import org.example.service.WeatherService;
import org.example.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Random;

@Service
public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository weatherRepository;

    private final WeatherMapper weatherMapper;

    Random random = new Random(55);

    public WeatherServiceImpl(WeatherRepository weatherRepository, WeatherMapper weatherMapper) {
        this.weatherRepository = weatherRepository;
        this.weatherMapper = weatherMapper;
    }

    public WeatherResponseDTO getWeatherForecast(Random rand) {
        Weather w = new Weather(rand);
        return weatherMapper.mapToResponseDTO(w);
    }

    @Override
    public WeatherResponseDTO getWeatherForecastOnToday(String city) {
        LocalDate date = DateUtils.getCurrentDate();
        weatherRepository.addWeatherForecast(city, date, weatherMapper.mapToWeather(this.getWeatherForecast(random)));
        return weatherMapper.mapToResponseDTO(weatherRepository.getWeatherForecast(city, date));
    }

    @Override
    public WeatherResponseDTO getWeatherForecastOnTomorrow(String city) {
        LocalDate date = DateUtils.getTomorrowDate();
        weatherRepository.addWeatherForecast(city, date, weatherMapper.mapToWeather(this.getWeatherForecast(random)));
        return weatherMapper.mapToResponseDTO(weatherRepository.getWeatherForecast(city, date));
    }

    @Override
    public HashMap<LocalDate, WeatherResponseDTO> getWeatherForecastOnTenDays(String city) {
        HashMap<LocalDate, WeatherResponseDTO> wf = new HashMap<>();
        LocalDate date = LocalDate.now();
        for (int i = 1; i <= 10; i++) {
            weatherRepository.addWeatherForecast(city, date, weatherMapper.mapToWeather(this.getWeatherForecast(random)));
            wf.put(date, weatherMapper.mapToResponseDTO(weatherRepository.getWeatherForecast(city, date)));
            date = date.plusDays(1);
        }
        return wf;
    }

    @Override
    public WeatherResponseDTO getWeatherForecastOnSetDay(String city, LocalDate date) {
        weatherRepository.addWeatherForecast(city, date, weatherMapper.mapToWeather(this.getWeatherForecast(random)));
        return weatherMapper.mapToResponseDTO(weatherRepository.getWeatherForecast(city, date));
    }
}
