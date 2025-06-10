package org.example.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic weatherForecastTopic() {
        return new NewTopic("weather-forecast-topic", 3, (short) 1);
    }

    @Bean
    public NewTopic popularCityTopic() {
        return new NewTopic("popular-city-topic", 3, (short) 1);
    }

    @Bean
    public NewTopic mostRequestsInHourTopic(){
        return new NewTopic("most-requests-in-hour-topic", 3, (short) 1);
    }
}
