package org.example.service.weather.Impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.WeatherApiResponseDTO;
import org.example.dto.WeatherResponseDTO;
import org.example.entity.*;
import org.example.mapper.WeatherMapper;
import org.example.repository.WeatherRepository;
import org.example.service.schedule.ScheduleService;
import org.example.service.weather.WeatherAPIService;
import org.example.service.wind.WindService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherRepository weatherRepository;
    private final WeatherMapper weatherMapper;

    private final WindService windService;
    private final WeatherAPIService weatherAPIService;
    private final WeatherDescriptionService weatherDescriptionService;
    private final ScheduleService scheduleService;

    public static final String CACHE_NAME = "weatherForecast";
    public static final String CACHE_KEY = "#city";


    public List<WeatherResponseDTO> getWeatherForecastOnDay(String city, LocalDate date) {
        if (checkDay(city, date)) {
            return getExistingForeCast(city, date);
        }
        else {
            return processAndSaveForecasts(city, date, weatherAPIService.getWeatherForecastOnDate(city, date));
        }
    }

    @Cacheable(cacheNames = CACHE_NAME, key = CACHE_KEY, cacheManager = "cacheManager")
    public List<WeatherResponseDTO> getWeatherForecastOnDays(String city, int countDays) {
        LocalDate dateStart = LocalDate.now();
        LocalDate dateEnd = LocalDate.now().plusDays(countDays - 1);
        if (isFullForecast(city, dateStart, dateEnd)) {
            return getExistingForeCastInRange(city, dateStart, dateEnd);
        }
        return weatherAPIService.getWeatherForecastOnDatesBetween(city, dateStart, dateEnd)
                .stream().map(response ->
                        mapWeatherApiResponseToDTO(response, city, response.getDate().toLocalDate()))
                .collect(Collectors.toList());
    }

    private Weather createOrUpdateWeather(WeatherApiResponseDTO weatherApiResponseDTO, Schedule schedule) {
        WeatherDescription weatherDesc = getWeatherDescription(weatherApiResponseDTO.getAtmosphereClouds());
        Weather weather = weatherRepository.findWeatherByScheduleId(schedule.getId());

        if (weather != null) {
            weather.setTemperature(weatherApiResponseDTO.getTemperature());
            weather.setHumidity(weatherApiResponseDTO.getHumidity());
            weather.setPressure(weatherApiResponseDTO.getPressure());
            weather.setWeatherDescription(weatherDesc);
        }
        else {
            weather = weatherMapper.mapToWeather(weatherApiResponseDTO);
            weather.setSchedule(schedule);
            weather.setWeatherDescription(weatherDesc);
        }

        return weatherRepository.save(weather);
    }


    private WeatherDescription getWeatherDescription(double cloudCover) {
        return weatherDescriptionService.getAllWeatherDescriptions().stream()
                .filter(d -> weatherDescriptionService.isInRange(cloudCover, d.getMinCloudCover(), d.getMaxCloudCover()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("WindDescription not found"));
    }

    private WeatherResponseDTO processForecast(WeatherApiResponseDTO response, String city, LocalDate date) {
        Schedule schedule = scheduleService.createSchedule(city, date, response.getDate().toLocalTime());
        Weather weather = createOrUpdateWeather(response, schedule);
        Wind wind = windService.createOrUpdateWind(response, schedule);
        WeatherResponseDTO res = weatherMapper.mapToResponseDTO(weather, wind);
        res.setDate(date);
        return res;
    }

    private List<WeatherResponseDTO> processAndSaveForecasts(String city, LocalDate date,
                                                     List<WeatherApiResponseDTO> responses) {
        return responses.stream()
                .map(response -> processForecast(response, city, date))
                .collect(Collectors.toList());
    }

    private WeatherResponseDTO mapWeatherApiResponseToDTO(WeatherApiResponseDTO response, String city,
                                                  LocalDate date) {
        LocalTime responseTime = response.getDate().toLocalTime();

        Schedule schedule = scheduleService.getScheduleByDateAndTime(city, date, responseTime)
                .orElseGet(() -> scheduleService.createSchedule(city, date, responseTime));

        Weather weather = createOrUpdateWeather(response, schedule);
        Wind wind = windService.createOrUpdateWind(response, schedule);
        WeatherResponseDTO result = weatherMapper.mapToResponseDTO(weather, wind);
        result.setDate(date);
        return result;
    }

    private boolean isFullForecast(String city, LocalDate dateStart, LocalDate dateEnd) {
        for (LocalDate date = dateStart; !date.isAfter(dateEnd); date = date.plusDays(1)) {
            if (scheduleService.getScheduleByDate(city, date).isEmpty()){
                return false;
            }
        }
        return true;
    }

    private boolean checkDay(String city, LocalDate date){
        return !scheduleService.getScheduleByDate(city, date).isEmpty();
    }

    private List<WeatherResponseDTO> getExistingForeCast(String city, LocalDate date) {
        return scheduleService.getScheduleByDate(city, date).stream()
                .map(sc -> {
                    Weather w = weatherRepository.findWeatherByScheduleId(sc.getId());
                    Wind wd = windService.getWindByScheduleId(sc.getId());
                    return weatherMapper.mapToResponseDTO(w, wd);})
                .collect(Collectors.toList());

    }

    private List<WeatherResponseDTO> getExistingForeCastInRange(String city, LocalDate dateStart, LocalDate dateEnd) {
        List<Schedule> schedules = scheduleService.getSchedulesBetween(city, dateStart, dateEnd);

        return schedules.stream().map(schedule -> {
            Weather weather = weatherRepository.findWeatherByScheduleId(schedule.getId());
            Wind wind = windService.getWindByScheduleId(schedule.getId());
            WeatherResponseDTO weatherResponseDTO = weatherMapper.mapToResponseDTO(weather, wind);
            weatherResponseDTO.setDate(schedule.getDate());
            return weatherResponseDTO;
        }).collect(Collectors.toList());
    }
}
