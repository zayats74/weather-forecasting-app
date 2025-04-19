package org.example.repository;

import org.example.entity.Weather;

import java.time.LocalDate;
import java.util.HashMap;

public class WeatherRepository {

    private HashMap<String, HashMap<LocalDate, Weather>> weatherForecast;

    public WeatherRepository() {
        weatherForecast = new HashMap<>();
    }

    public void addWeatherForecast(String city, LocalDate date, Weather weather){
       if(!weatherForecast.containsKey(city)){
           weatherForecast.put(city, new HashMap<>());
       }
       if(!weatherForecast.get(city).containsKey(date)){
           weatherForecast.get(city).put(date, weather);
       }
    }

    public Weather getWeatherForecast(String city, LocalDate date){
        return weatherForecast.get(city).get(date);
    }


}
