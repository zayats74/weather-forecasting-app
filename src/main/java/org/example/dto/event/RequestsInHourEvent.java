package org.example.dto.event;

import java.time.LocalTime;

public record RequestsInHourEvent(
        LocalTime time,
        long requestCount

) {}
