package org.example.dto.event;

import java.time.LocalTime;

public record WeatherRequestEvent (
        String city,
        LocalTime requestTime
) {}
