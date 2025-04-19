package org.example.service.Impl;

import org.example.entity.Weather;
import org.example.repository.WeatherRepository;
import org.example.service.DateService;
import org.example.service.WeatherService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Random;

public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository weatherRepository;
    private final DateService dateService;

    Random random = new Random(55);

    public WeatherServiceImpl(WeatherRepository weatherRepository, DateService dateService) {
        this.weatherRepository = weatherRepository;
        this.dateService = dateService;
    }

    public Weather getWeatherForecast(Random rand) {
        return new Weather(rand);
    }

    @Override
    public Weather getWeatherForecastOnToday(String city) {
        LocalDate date = dateService.getCurrentDate();
        weatherRepository.addWeatherForecast(city, date, this.getWeatherForecast(random));
        return weatherRepository.getWeatherForecast(city, date);
    }

    @Override
    public Weather getWeatherForecastOnTomorrow(String city) {
        LocalDate date = dateService.getTomorrowDate();
        weatherRepository.addWeatherForecast(city, date, this.getWeatherForecast(random));
        return weatherRepository.getWeatherForecast(city, date);
    }

    @Override
    public HashMap<LocalDate, Weather> getWeatherForecastOnTenDays(String city) {
        HashMap<LocalDate, Weather> wf = new HashMap<>();
        LocalDate date = LocalDate.now();
        for (int i = 1; i <= 10; i++) {
            weatherRepository.addWeatherForecast(city, date, this.getWeatherForecast(random));
            wf.put(date, weatherRepository.getWeatherForecast(city, date));
            date = date.plusDays(1);
        }
        return wf;
    }

    @Override
    public Weather getWeatherForecastOnSetDay(String city, LocalDate date) {
        weatherRepository.addWeatherForecast(city, date, this.getWeatherForecast(random));
        return weatherRepository.getWeatherForecast(city, date);
    }
}
