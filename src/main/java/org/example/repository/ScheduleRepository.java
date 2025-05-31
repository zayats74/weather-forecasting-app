package org.example.repository;

import org.example.entity.City;
import org.example.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
    @Query("SELECT COUNT(s) FROM Schedule s WHERE s.city = :city AND s.date = :date")
    int countByCityAndDate(@Param("city") City city, @Param("date") LocalDate date);

    @Query("SELECT s FROM Schedule s WHERE s.city = :city AND s.date = :date")
    List<Schedule> findByCityAndDate(@Param("city") City city, @Param("date") LocalDate date);

    List<Schedule> findByCityAndDateBetween(City city, LocalDate dateStart, LocalDate dateEnd);

    Optional<Schedule> findByCityAndDateAndTime(City cityEntity, LocalDate date, LocalTime responseTime);
}
