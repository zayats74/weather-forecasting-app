package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherApiResponseDTO {

    @JsonProperty("dt_forecast")
    private LocalDateTime date;

    @JsonProperty("temp_2")
    private double temperature;

    @JsonProperty("vlaga_2f")
    private double humidity;

    @JsonProperty("pres_surf")
    private int pressure;

    @JsonProperty("uv_index")
    private int uvIndex;

    @JsonProperty("vidimost_surf")
    private int visibility;

    @JsonProperty("wind_speed_10")
    private double windSpeed;

    @JsonProperty("wind_dir_10")
    private double windDirection;

    @JsonProperty("oblachnost_atmo")
    private double atmosphereClouds;
}
