package org.example.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Wind {

    private String direction;

    //in km/h
    private double speed;

    //in km/s
    private double gusts;

    //Getters
    public String getDirection() {
        return direction;
    }

    public double getSpeed() {
        return speed;
    }

    public double getGusts() {
        return gusts;
    }

    String[] dirEnums = {"северо-восток", "юго-запад", "юг", "север", "восток", "запад",
            "северо-запад", "юго-восток"};
    List<String> directionEnums = new ArrayList<>(Arrays.asList(dirEnums));

    //Constructors
    public Wind(){}

    public Wind(String direction, double speed, double gusts) {
        this.direction = direction;
        this.speed = speed;
        this.gusts = gusts;
    }

    public Wind(Random rand){
        this.direction = directionEnums.get(rand.nextInt(directionEnums.size()));
        this.speed = rand.nextDouble(0, 100);
        this.gusts = rand.nextDouble(0, 100);
    }

    //Methods
    //Print characteristics of wind in Console
    @Override
    public String toString(){
        return String.format("\tНаправление: %s\n\tСкорость, км/ч: %.1f\n\tПорывы ветра, км/ч: %.1f",
                direction, speed, gusts);
    }

}