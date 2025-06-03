package org.example.dto.event;


public record RequestsInHourEvent(
        int hour,
        long requestCount

) {}
