package org.example.service.Impl;

import org.example.config.WeatherForecastProperties;
import org.example.dto.WeatherApiResponseDTO;
import org.example.dto.WeatherResponseDTO;
import org.example.entity.*;
import org.example.mapper.WeatherMapper;
import org.example.mapper.WindMapper;
import org.example.repository.*;
import org.example.service.CityService;
import org.example.service.WeatherService;
import org.example.utils.DateUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository weatherRepository;
    private final ScheduleRepository scheduleRepository;
    private final WindRepository windRepository;
    private final CityRepository cityRepository;
    private final WeatherDescriptionRepository weatherDescriptionRepository;
    private final WeatherMapper weatherMapper;
    private final WindMapper windMapper;
    private final RestClient weatherClient;
    private final WeatherForecastProperties weatherForecastProperties;
    private final CityService cityService;
    private final WindDescriptionRepository windDescriptionRepository;

    public static final String URI_DAY_FORECAST = "/api/weather/?lat={lat}&lon={lon}&date={date}&token={token}";
    public static final String URI_DAYS_FORECAST = "/api/weather/?lat={lat}&lon={lon}&date={dateStart},{dateEnd}&token={token}";
    public static final String COORDINATES_SEPARATOR = ", ";

    public WeatherServiceImpl(WeatherRepository weatherRepository, ScheduleRepository scheduleRepository, WindRepository windRepository, CityRepository cityRepository, WeatherDescriptionRepository weatherDescriptionRepository, WeatherMapper weatherMapper, WindMapper windMapper,
                              RestClient weatherClient, WeatherForecastProperties weatherForecastProperties, CityService cityService, WindDescriptionRepository windDescriptionRepository) {
        this.weatherRepository = weatherRepository;
        this.scheduleRepository = scheduleRepository;
        this.windRepository = windRepository;
        this.cityRepository = cityRepository;
        this.weatherDescriptionRepository = weatherDescriptionRepository;
        this.weatherMapper = weatherMapper;
        this.windMapper = windMapper;
        this.weatherClient = weatherClient;
        this.weatherForecastProperties = weatherForecastProperties;
        this.cityService = cityService;
        this.windDescriptionRepository = windDescriptionRepository;
    }

    private List<WeatherResponseDTO> processAndSaveForecasts(City city, LocalDate date,
                                                             List<WeatherApiResponseDTO> responses) {
        return responses.stream()
                .map(response -> processForecast(response, city, date))
                .toList();
    }

    @Override
    public List<WeatherResponseDTO> getWeatherForecastOnAllHours(String city, LocalDate date) {
        if (checkFullDay(cityRepository.findByCity(city), date)) {
            return getExistingForeCast(cityRepository.findByCity(city), date);
        }
        else {
            String[] coordinates = cityService.getCityCoordinates(city).split(", ");
            List <WeatherApiResponseDTO> responseApi =  weatherClient
                    .get()
                    .uri(URI_DAY_FORECAST,
                            coordinates[0], coordinates[1], date, weatherForecastProperties.getToken())
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<WeatherApiResponseDTO>>() {});

            return processAndSaveForecasts(cityRepository.findByCity(city), date, responseApi);
        }
    }

    private List<WeatherResponseDTO> getExistingForeCast(City city, LocalDate date) {
        return scheduleRepository.findByCityAndDate(city, date).stream()
                .map(sc -> {
                    Weather w = weatherRepository.findWeatherByScheduleId(sc.getId());
                    Wind wd = windRepository.findByScheduleId(sc.getId());
                return weatherMapper.mapToResponseDTO(w, wd);})
                .toList();

    }

    @Override
    public List<WeatherResponseDTO> getWeatherForecastOnDates(String city, LocalDate dateStart, LocalDate dateEnd) {
        String[] coordinates = cityService.getCityCoordinates(city).split(COORDINATES_SEPARATOR);
        List<WeatherApiResponseDTO> responseApi =  weatherClient
                .get()
                .uri(URI_DAYS_FORECAST,
                        coordinates[0], coordinates[1], dateStart, dateEnd, weatherForecastProperties.getToken())
                .retrieve()
                .body(new ParameterizedTypeReference<List<WeatherApiResponseDTO>>() {});

        return responseApi.stream()
                .map(response ->
                        mapWeatherApiResponseToDTO(response, city))
                .toList();
    }

    @Override
    public List<WeatherResponseDTO> getWeatherForecastOnToday(String city) {
        LocalDate date = DateUtils.getCurrentDate();
        return getWeatherForecastOnAllHours(city, date);
    }

    @Override
    public List<WeatherResponseDTO> getWeatherForecastOnTomorrow(String city) {
        LocalDate date = DateUtils.getTomorrowDate();
        return getWeatherForecastOnAllHours(city, date);
    }

    @Override
    public List<WeatherResponseDTO> getWeatherForecastOnTenDays(String city) {
        LocalDate dateStart = LocalDate.now();
        LocalDate dateEnd = dateStart.plusDays(9);
        return this.getWeatherForecastOnDates(city, dateStart, dateEnd);
    }

    @Override
    public List<WeatherResponseDTO> getWeatherForecastOnSetDay(String city, LocalDate date) {
        return getWeatherForecastOnAllHours(city, date);
    }

    private boolean checkFullDay(City city, LocalDate date){
        return scheduleRepository.countByCityAndDate(city, date) == 24;
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

    private Weather createWeather(WeatherApiResponseDTO weatherApiResponseDTO, Schedule schedule) {
        WeatherDescription weatherDesc = weatherDescriptionRepository.findByCloudCover(
                weatherApiResponseDTO.getAtmosphereClouds());
        Weather weather = weatherMapper.mapToWeather(weatherApiResponseDTO);
        weather.setSchedule(schedule);
        weather.setWeatherDescription(weatherDesc);
        return weatherRepository.save(weather);
    }

    private Wind createWind(WeatherApiResponseDTO weatherApiResponseDTO, Schedule schedule) {
        WindDescription windDesc = windDescriptionRepository.findByDegree(
                normalizeDegrees(weatherApiResponseDTO.getWindDirection()));
        Wind wind = windMapper.mapToWind(weatherApiResponseDTO);
        wind.setSchedule(schedule);
        wind.setWindDescription(windDesc);
        return windRepository.save(wind);
    }

    private double normalizeDegrees(double degrees) {
        degrees = degrees % 360;
        return degrees < 0 ? degrees + 360 : degrees;
    }

    private WeatherResponseDTO processForecast(WeatherApiResponseDTO response, City city, LocalDate date) {
        Schedule schedule = createSchedule(city, date, response.getDate().toLocalTime());
        Weather weather = createWeather(response, schedule);
        Wind wind = createWind(response, schedule);
        WeatherResponseDTO res = weatherMapper.mapToResponseDTO(weather, wind);
        res.setDate(date);
        return res;
    }

    private WeatherResponseDTO mapWeatherApiResponseToDTO(WeatherApiResponseDTO response, String city) {
        LocalDate responseDate = response.getDate().toLocalDate();
        LocalTime responseTime = response.getDate().toLocalTime();

        Schedule schedule = createSchedule(cityRepository.findByCity(city), responseDate, responseTime);

        Weather weather = createWeather(response, schedule);
        Wind wind = createWind(response, schedule);
        WeatherResponseDTO result = weatherMapper.mapToResponseDTO(weather, wind);
        result.setDate(responseDate);
        return result;
    }
}
