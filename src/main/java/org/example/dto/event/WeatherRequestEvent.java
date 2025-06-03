package org.example.dto.event;

import java.time.LocalDateTime;

public record WeatherRequestEvent (
        String city,
        LocalDateTime requestTime
) {}
