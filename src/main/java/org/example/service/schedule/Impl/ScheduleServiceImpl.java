package org.example.service.schedule.Impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.City;
import org.example.entity.Schedule;
import org.example.repository.ScheduleRepository;
import org.example.service.city.CityService;
import org.example.service.schedule.ScheduleService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CityService cityService;

    @Override
    public Schedule createSchedule(String city, LocalDate date, LocalTime time) {
        City cityEntity = cityService.getCityByName(city);
        Schedule schedule = Schedule.builder()
                .city(cityEntity)
                .date(date)
                .time(time)
                .createdAt(LocalDateTime.now())
                .build();
        return scheduleRepository.save(schedule);
    }

    @Override
    public List<Schedule> getSchedulesBetween(String city, LocalDate dateStart, LocalDate dateEnd){
        return scheduleRepository.findByCityAndDateBetween(
                cityService.getCityByName(city), dateStart, dateEnd);
    }

    @Override
    public Optional<Schedule> getScheduleByDateAndTime(String city, LocalDate date, LocalTime time){
        City cityEntity = cityService.getCityByName(city);
        return  scheduleRepository.findByCityAndDateAndTime(cityEntity, date, time);
    }

    @Override
    public List<Schedule> getScheduleByDate(String city, LocalDate date){
        City cityEntity = cityService.getCityByName(city);
        return  scheduleRepository.findByCityAndDate(cityEntity, date);
    }
}
