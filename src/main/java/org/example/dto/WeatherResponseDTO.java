package org.example.dto;

import lombok.*;
import org.example.entity.Wind;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherResponseDTO {

    private LocalDate date;

    private double temperature;

    private double humidity;

    private int pressure;

    private int uvIndex;

    private int visibility;

    private String description;

    private Wind wind;

}
