package org.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.yaml")
public class AppProperties {

    @Value("${city-coordinates.baseUrl}")
    private String cityBaseUrl;

    @Value("${weather-forecast.baseUrl}")
    private String weatherBaseUrl;

    @Value("${weather-forecast.token}")
    private String weatherToken;

    @Value("${city-coordinates.api-key}")
    private String cityApiKey;

    //Getters
    public String getCityBaseUrl() {
        return cityBaseUrl;
    }

    public String getWeatherBaseUrl() {
        return weatherBaseUrl;
    }

    public String getWeatherToken() {
        return weatherToken;
    }

    public String getCityApiKey() {
        return cityApiKey;
    }
}
