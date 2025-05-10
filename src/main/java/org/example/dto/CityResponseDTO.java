package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CityResponseDTO {

    @JsonProperty("response")
    private Response meta;

    private static class Response {
        @JsonProperty("GeoObjectCollection")
        private GeoObjectCollection geoObjectCollection;
    }

    private static class GeoObjectCollection {
        @JsonProperty("featureMember")
        private List<FeatureMember> featureMembers;
    }

    private static class FeatureMember {
        @JsonProperty("GeoObject")
        private GeoObject geoObject;
    }

    private static class GeoObject {
        @JsonProperty("Point")
        private Point point;
    }

    private static class Point {
        @JsonProperty("pos")
        private String coordinates;
    }

    public double[] getCoordinates() {
        String[] coor = meta
                .geoObjectCollection
                .featureMembers.get(0)
                .geoObject
                .point
                .coordinates
                .split(" ");
        return new double[] {
                Double.parseDouble(coor[0]),
                Double.parseDouble(coor[1]),
        };
    }
}
