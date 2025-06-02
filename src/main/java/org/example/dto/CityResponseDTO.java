package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@Schema(description = "DTO для получения координат города из внешнего геокодера")
public class CityResponseDTO {

    @Schema(description = "Ответ")
    @JsonProperty("response")
    private Response meta;

    @Schema(description = "Класс ответа")
    private static class Response {
        @Schema(description = "Коллекция географических объектов")
        @JsonProperty("GeoObjectCollection")
        private GeoObjectCollection geoObjectCollection;
    }

    @Schema(description = "Класс для коллекции геообъектов")
    private static class GeoObjectCollection {
        @Schema(description = "Список элементов с геоданными")
        @JsonProperty("featureMember")
        private List<FeatureMember> featureMembers;
    }

    @Schema(description = "Элемент коллекции с геообъектом")
    private static class FeatureMember {
        @Schema(description = "Географический объект")
        @JsonProperty("GeoObject")
        private GeoObject geoObject;
    }

    @Schema(description = "Геообъект, содержащий точку координат")
    private static class GeoObject {
        @Schema(description = "Точка с координатами")
        @JsonProperty("Point")
        private Point point;
    }

    @Schema(description = "Координатная точка")
    private static class Point {
        @Schema(
                description = "Координаты в формате 'долгота широта'",
                example = "37.6174943 55.7558261"
        )
        @JsonProperty("pos")
        private String coordinates;
    }

    @Schema(description = "Извлекает координаты из ответа", hidden = true)
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
