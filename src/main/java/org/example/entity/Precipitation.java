package org.example.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Precipitation {
    //description of precipitation
    private String description;

    //in mm
    private int precipitation;

    String[] desEnums = {"дождь", "снег", "град", "без осадков", "ливень", "туман"};
    List<String> descriptionEnums = new ArrayList<>(Arrays.asList(desEnums));

    //Getters
    public String getDescription() {
        return description;
    }

    public int getPrecipitation() {
        return precipitation;
    }

    //Constructors
    public Precipitation(){}

    public Precipitation(String description, int precipitation) {
        this.description = description;
        this.precipitation = precipitation;
    }

    public Precipitation(Random rand){
        this.description = descriptionEnums.get(rand.nextInt(descriptionEnums.size()));
        this.precipitation = rand.nextInt(0, 100);
    }

    //Methods
    @Override
    public String toString(){

        return "\tОписание: "+description+"\n\tОсадки, мм: "+precipitation;
    }
}