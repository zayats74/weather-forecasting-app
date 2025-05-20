package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "wind")
public class Wind {

    @Id
    @Column(name = "id_schedule")
    private Long scheduleId;

    //in km/h
    private double speed;


    @OneToOne
    @JoinColumn(name = "id_schedule")
    @MapsId
    private Schedule schedule;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_wind_desc")
    private WindDescription windDescription;

}