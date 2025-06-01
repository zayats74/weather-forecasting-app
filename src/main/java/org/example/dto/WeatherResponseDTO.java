package org.example.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.example.entity.WeatherDescription;
import org.example.entity.Wind;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Прогноз погоды на конкретную дату")
public class WeatherResponseDTO {
    @Schema(
            description = "Дата прогноза в формате YYYY-MM-DD",
            example = "2023-06-15"
    )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Schema(
            description = "Температура в градусах Цельсия",
            example = "22.5"
    )
    private double temperature;

    @Schema(
            description = "Относительная влажность воздуха в процентах",
            example = "65.0"
    )
    private double humidity;

    @Schema(
            description = "Атмосферное давление в Па",
            example = "101500"
    )
    private int pressure;

    @Schema(
            description = "Ультрафиолетовый индекс (0-11)",
            example = "7"
    )
    private int uvIndex;

    @Schema(
            description = "Видимость в метрах",
            example = "10000"
    )
    private int visibility;

    @Schema(
            description = "Описание погодных условий",
            implementation = WeatherDescription.class
    )
    private WeatherDescription description;

    @Schema(
            description = "Параметры ветра",
            implementation = Wind.class
    )
    private Wind wind;

    @Schema(hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate getDate() {
        return date;
    }

}
