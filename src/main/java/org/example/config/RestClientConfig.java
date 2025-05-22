package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;


@Configuration
public class RestClientConfig {

    private final CityCoordinatesProperties cityCoordinatesProperties;

    private final WeatherForecastProperties weatherForecastProperties;

    public RestClientConfig(CityCoordinatesProperties cityCoordinatesProperties, WeatherForecastProperties weatherForecastProperties) {
        this.cityCoordinatesProperties = cityCoordinatesProperties;
        this.weatherForecastProperties = weatherForecastProperties;
    }


    @Bean
    public RestClient сityClient(RestClient.Builder builder) {
        return builder
                .baseUrl(cityCoordinatesProperties.getBaseUrl())
                .defaultHeader("accept", "application/json")
                .build();
    }

    @Bean
    public RestClient weatherClient(RestClient.Builder builder) {
        return builder
                .baseUrl(weatherForecastProperties.getBaseUrl())
                .build();
    }
}
