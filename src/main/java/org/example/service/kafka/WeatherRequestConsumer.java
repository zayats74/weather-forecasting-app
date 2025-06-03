package org.example.service.kafka;

import lombok.RequiredArgsConstructor;
import org.example.dto.event.WeatherRequestEvent;
import org.example.service.statistics.StatisticsService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherRequestConsumer {

    private final StatisticsService statisticsService;

    @KafkaListener(topics = "weather-forecast-topic", groupId = "weather-statistics")
    public void consume(WeatherRequestEvent event) {
        statisticsService.saveRequest(event);
        statisticsService.sendStatistics();
    }
}
