package org.example.service.scheduleServices;

import org.example.entity.Schedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleService {
    Schedule createSchedule(String city, LocalDate date, LocalTime time);

    List<Schedule> getSchedulesBetween(String city, LocalDate dateStart, LocalDate dateEnd);

    Optional<Schedule> getScheduleByDateAndTime(String city, LocalDate date, LocalTime time);

    List<Schedule> getScheduleByDate(String city, LocalDate date);
}
