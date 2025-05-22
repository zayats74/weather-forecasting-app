package org.example.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherRequestDTO {
    private UUID id;

    private String city;

    private LocalDate date;
}
