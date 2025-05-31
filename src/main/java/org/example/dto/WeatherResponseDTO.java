package org.example.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.example.entity.WeatherDescription;
import org.example.entity.Wind;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherResponseDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    private double temperature;

    private double humidity;

    private int pressure;

    private int uvIndex;

    private int visibility;

    private WeatherDescription description;

    private Wind wind;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate getDate() {
        return date;
    }

}
