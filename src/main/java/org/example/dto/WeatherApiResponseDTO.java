package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Ответ от API метеопрогноза")
public class WeatherApiResponseDTO {

    @Schema(
            description = "Дата и время прогноза погоды в формате ISO 8601",
            example = "2023-05-15T12:00:00"
    )
    @JsonProperty("dt_forecast")
    private LocalDateTime date;

    @Schema(
            description = "Температура воздуха в градусах Цельсия",
            example = "20.5"
    )
    @JsonProperty("temp_2")
    private double temperature;

    @Schema(
            description = "Относительная влажность воздуха в процентах",
            example = "65.0"
    )
    @JsonProperty("vlaga_2f")
    private double humidity;

    @Schema(
            description = "Атмосферное давление в гектопаскалях (Па)",
            example = "760000"
    )
    @JsonProperty("pres_surf")
    private int pressure;

    @Schema(
            description = "Ультрафиолетовый индекс (0-11)",
            example = "5"
    )
    @JsonProperty("uv_index")
    private int uvIndex;

    @Schema(
            description = "Видимость в метрах",
            example = "10000"
    )
    @JsonProperty("vidimost_surf")
    private int visibility;

    @Schema(
            description = "Скорость ветра в метрах в секунду",
            example = "5.8"
    )
    @JsonProperty("wind_speed_10")
    private double windSpeed;

    @Schema(
            description = "Направление ветра в градусах",
            example = "180.0"
    )
    @JsonProperty("wind_dir_10")
    private double windDirection;

    @Schema(
            description = "Облачность в процентах",
            example = "75.0"
    )
    @JsonProperty("oblachnost_atmo")
    private double atmosphereClouds;
}
