package org.example.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Weather {

    //actual temperature
    private double temperature;

    //in percentages
    private double humidity;

    //in kPas
    private int pressure;

    //from 0 to 11
    private int uvIndex;

    //in km
    private int visibility;

    private String description;

    //all wind charachteristics
    private Wind wind;

    //all precipation charachteristics
    private Precipitation precipation;

    String[] desEnums = {"ясно", "облачно", "переменная облачность", "облачно с прояснениями"};
    List<String> descriptionEnums = new ArrayList<>(Arrays.asList(desEnums));

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

    public String getWind() {
        return wind.toString();
    }

    public String getPrecipation() {
        return precipation.toString();
    }


    //Constructors
    public Weather(){}

    public Weather(Random rand){

        this.temperature = rand.nextDouble(-100, 100);
        this.humidity = rand.nextDouble(0, 1);
        this.pressure = rand.nextInt(90, 105);
        this.uvIndex = rand.nextInt(0, 11);
        this.visibility = rand.nextInt(0, 45);
        this.description = descriptionEnums.get(rand.nextInt(descriptionEnums.size()));
        this.wind = new Wind(rand);
        this.precipation = new Precipitation(rand);
    }

    //Methods
    @Override
    public String toString() {
        return String.format(
                "Фактическая температуа, С: %.1f" +
                "\nВлажность, %%: %.0f" +
                "\nДавление, кПа: %d" +
                "\nУФ индекс: %d" +
                "\nВидимость, км: %d" +
                "\nПогодные условия: %s" +
                "\nВетер:\n%s" +
                "\nОсадки:\n%s",
                temperature, humidity * 100,
                pressure, uvIndex, visibility,
                description, wind.toString(), precipation.toString());
    }

}