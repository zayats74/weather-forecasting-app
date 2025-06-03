package org.example.dto.event;

public record CityCountEvent(
        String city,
        long requestCount
) {}
