package org.example.service.statistics;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.dto.event.CityCountEvent;
import org.example.dto.event.RequestsInHourEvent;
import org.example.dto.event.WeatherRequestEvent;
import org.example.entity.City;
import org.example.entity.UserRequest;
import org.example.repository.UserRequestRepository;
import org.example.service.city.CityService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final UserRequestRepository userRequestRepository;
    private final CityService cityService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public void saveRequest(WeatherRequestEvent event){
        City city = cityService.getCityByName(event.city());

        UserRequest userRequest = UserRequest.builder()
                .city(city)
                .requestedAt(event.requestTime())
                .build();
        userRequestRepository.save(userRequest);
    }

    public void sendStatistics(){
        LocalDateTime lastMonth = LocalDateTime.now().minusMonths(1);

        //city statistics
        List<Object[]> cityStats = userRequestRepository.getCityStats(lastMonth);
        if (!cityStats.isEmpty()) {
            Object[] topCity = cityStats.get(0);
            kafkaTemplate.send("popular-city-topic",
                    new CityCountEvent((String) topCity[0], (Long) topCity[1]));
        }

        //request for hour statistics
        List<Object[]> hourStats = userRequestRepository.getHourStats(lastMonth);
        if (!hourStats.isEmpty()) {
            Object[] topHour = hourStats.get(0);
            kafkaTemplate.send("most-requests-in-hour-topic",
                    new RequestsInHourEvent((Integer) topHour[0], (Long) topHour[1]));
        }

    }

}
