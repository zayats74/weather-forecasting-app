package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    private final AppProperties appProperties;

    public RestClientConfig(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Bean
    public RestClient сityClient() {
        return RestClient.builder()
                .baseUrl(appProperties.getCityBaseUrl())
                .defaultHeader("accept", "application/json")
                .build();
    }

    @Bean
    public RestClient weatherClient() {
        return RestClient.builder()
                .baseUrl(appProperties.getWeatherBaseUrl())
                .build();
    }
}
