package org.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "weather-forecast")
public class WeatherForecastProperties {

    private String baseUrl;

    private String token;


    //Getters
    public String getBaseUrl() {
        return baseUrl;
    }

    public String getToken() {
        return token;
    }


    //Setters
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
