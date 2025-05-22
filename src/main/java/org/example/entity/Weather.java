package org.example.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "weather")
public class Weather {

    @Id
    @Column(name = "id_schedule")
    private Long scheduleId;

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

    @OneToOne
    @JoinColumn(name = "id_schedule")
    @MapsId
    private Schedule schedule;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_weather_desc")
    private WeatherDescription weatherDescription;

}