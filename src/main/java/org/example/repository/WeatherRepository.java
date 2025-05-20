package org.example.repository;

import org.example.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;


import java.util.UUID;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, UUID> {

    Weather findWeatherByScheduleId(Long id);
}
