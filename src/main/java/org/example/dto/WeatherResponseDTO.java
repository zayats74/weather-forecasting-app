package org.example.dto;

import org.example.entity.Precipitation;
import org.example.entity.Wind;


public class WeatherResponseDTO {
    private double temperature;

    private double humidity;

    private int pressure;

    private int uvIndex;

    private int visibility;

    private String description;

    private Wind wind;

    private Precipitation precipitation;

    //Getters
    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public int getUvIndex() {
        return uvIndex;
    }

    public int getVisibility() {
        return visibility;
    }

    public String getDescription() {
        return description;
    }

    public Wind getWind() {
        return wind;
    }

    public Precipitation getPrecipitation() {
        return precipitation;
    }


    //Setters
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public void setUvIndex(int uvIndex) {
        this.uvIndex = uvIndex;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public void setPrecipitation(Precipitation precipitation) {
        this.precipitation = precipitation;
    }


    //Constructors
    public WeatherResponseDTO() {}

    public WeatherResponseDTO(double temperature, double humidity, int pressure, int uvIndex,
                   int visibility, String description, Wind wind,
                   Precipitation precipitation) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.uvIndex = uvIndex;
        this.visibility = visibility;
        this.description = description;
        this.wind = wind;
        this.precipitation = precipitation;
    }
}
