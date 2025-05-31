package org.example.service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.config.WeatherForecastProperties;
import org.example.dto.WeatherApiResponseDTO;
import org.example.dto.WeatherResponseDTO;
import org.example.entity.*;
import org.example.mapper.WeatherMapper;
import org.example.mapper.WindMapper;
import org.example.repository.CityRepository;
import org.example.repository.ScheduleRepository;
import org.example.repository.WeatherRepository;
import org.example.repository.WindRepository;
import org.example.service.CityService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeatherForecastDataService {
    private final ScheduleRepository scheduleRepository;
    private final WeatherRepository weatherRepository;
    private final CityRepository cityRepository;
    private final WeatherMapper weatherMapper;
    private final WindMapper windMapper;
    private final WindRepository windRepository;
    private final WindDescriptionService windDescriptionService;
    private final WeatherDescriptionService weatherDescriptionService;
    private final CityService cityService;
    private final RestClient weatherClient;
    private final WeatherForecastProperties weatherForecastProperties;

    public static final String URI_DAY_FORECAST = "/api/weather/?lat={lat}&lon={lon}&date={date}&token={token}";
    public static final String URI_DAYS_FORECAST = "/api/weather/?lat={lat}&lon={lon}&date={dateStart},{dateEnd}&token={token}";
    public static final String COORDINATES_SEPARATOR = ", ";


    public List<WeatherResponseDTO> getWeatherForecastOnDate(String city, LocalDate date) {
        String[] coordinates = cityService.getCityCoordinates(city).split(", ");
        City cityEntity = cityRepository.findByCity(city);
        if (checkDay(cityEntity, date)) {
            return getExistingForeCast(cityEntity, date);
        }
        List<WeatherApiResponseDTO> responseApi = weatherClient
                .get()
                .uri(URI_DAY_FORECAST,
                        coordinates[0], coordinates[1], date, weatherForecastProperties.getToken())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        return processAndSaveForecasts(cityRepository.findByCity(city), date, responseApi);
    }

    public List<WeatherResponseDTO> getWeatherForecastOnDatesBetween(String city, LocalDate dateStart, LocalDate dateEnd) {
        String[] coordinates = cityService.getCityCoordinates(city).split(COORDINATES_SEPARATOR);
        List<WeatherApiResponseDTO> responseApi = weatherClient
                .get()
                .uri(URI_DAYS_FORECAST,
                        coordinates[0], coordinates[1], dateStart, dateEnd, weatherForecastProperties.getToken())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        return responseApi.stream()
                .map(response ->
                        mapWeatherApiResponseToDTO(response, city, response.getDate().toLocalDate()))
                .collect(Collectors.toList());
    }


    private Schedule createSchedule(City city, LocalDate date, LocalTime time) {
        Schedule schedule = Schedule.builder()
                .city(city)
                .date(date)
                .time(time)
                .createdAt(LocalDateTime.now())
                .build();
        return scheduleRepository.save(schedule);
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

    private Wind createOrUpdateWind(WeatherApiResponseDTO weatherApiResponseDTO, Schedule schedule) {
        WindDescription windDesc = getWindDescription(weatherApiResponseDTO.getWindDirection());
        Wind wind = windRepository.findByScheduleId(schedule.getId());

        if (wind != null) {
            wind.setSpeed(weatherApiResponseDTO.getWindSpeed());
            wind.setWindDescription(windDesc);
        }
        else {
            wind = windMapper.mapToWind(weatherApiResponseDTO);
            wind.setSchedule(schedule);
            wind.setWindDescription(windDesc);
        }

        return windRepository.save(wind);
    }

    private WeatherDescription getWeatherDescription(double cloudCover) {
        return weatherDescriptionService.findByCloudCover(cloudCover)
                .orElseThrow(() -> new RuntimeException("WindDescription not found"));
    }

    private WindDescription getWindDescription(double degrees) {
        return windDescriptionService.findByDegree(degrees)
                .orElseThrow(() -> new RuntimeException("WindDescription not found"));
    }

    private WeatherResponseDTO processForecast(WeatherApiResponseDTO response, City city, LocalDate date) {
        Schedule schedule = createSchedule(city, date, response.getDate().toLocalTime());
        Weather weather = createOrUpdateWeather(response, schedule);
        Wind wind = createOrUpdateWind(response, schedule);
        WeatherResponseDTO res = weatherMapper.mapToResponseDTO(weather, wind);
        res.setDate(date);
        return res;
    }

    private WeatherResponseDTO mapWeatherApiResponseToDTO(WeatherApiResponseDTO response, String city,
                                                          LocalDate date) {
        LocalTime responseTime = response.getDate().toLocalTime();
        City cityEntity = cityRepository.findByCity(city);

        Schedule schedule = scheduleRepository.findByCityAndDateAndTime(cityEntity, date, responseTime)
                .orElseGet(() -> createSchedule(cityEntity, date, responseTime));

        Weather weather = createOrUpdateWeather(response, schedule);
        Wind wind = createOrUpdateWind(response, schedule);
        WeatherResponseDTO result = weatherMapper.mapToResponseDTO(weather, wind);
        result.setDate(date);
        return result;
    }

    private List<WeatherResponseDTO> processAndSaveForecasts(City city, LocalDate date,
                                                             List<WeatherApiResponseDTO> responses) {
        return responses.stream()
                .map(response -> processForecast(response, city, date))
                .collect(Collectors.toList());
    }

    public boolean isFullForecast(String city, LocalDate dateStart, LocalDate dateEnd) {
        for (LocalDate date = dateStart; !date.isAfter(dateEnd); date = date.plusDays(1)) {
            if (scheduleRepository.findByCityAndDate(cityRepository.findByCity(city), date).isEmpty()){
                return false;
            }
        }
        return true;
    }

    public boolean checkDay(City city, LocalDate date){
        return !scheduleRepository.findByCityAndDate(city, date).isEmpty();
    }

    public List<WeatherResponseDTO> getExistingForeCast(City city, LocalDate date) {
        return scheduleRepository.findByCityAndDate(city, date).stream()
                .map(sc -> {
                    Weather w = weatherRepository.findWeatherByScheduleId(sc.getId());
                    Wind wd = windRepository.findByScheduleId(sc.getId());
                    return weatherMapper.mapToResponseDTO(w, wd);})
                .collect(Collectors.toList());

    }

    public List<WeatherResponseDTO> getExistingForeCastInRange(String city, LocalDate dateStart, LocalDate dateEnd) {
        List<Schedule> schedules = scheduleRepository.findByCityAndDateBetween(
                cityRepository.findByCity(city), dateStart, dateEnd);

        return schedules.stream().map(schedule -> {
            Weather weather = weatherRepository.findWeatherByScheduleId(schedule.getId());
            Wind wind = windRepository.findByScheduleId(schedule.getId());
            WeatherResponseDTO weatherResponseDTO = weatherMapper.mapToResponseDTO(weather, wind);
            weatherResponseDTO.setDate(schedule.getDate());
            return weatherResponseDTO;
        }).collect(Collectors.toList());
    }
}
