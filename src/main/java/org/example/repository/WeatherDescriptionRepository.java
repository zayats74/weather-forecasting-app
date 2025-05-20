package org.example.repository;

import org.example.entity.WeatherDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WeatherDescriptionRepository extends JpaRepository<WeatherDescription, Long> {

    @Query("SELECT wd FROM WeatherDescription wd WHERE wd.minCloudCover <= :cloudCover " +
            "AND :cloudCover <= wd.maxCloudCover")
    WeatherDescription findByCloudCover(@Param("cloudCover") double cloudCover);
}
