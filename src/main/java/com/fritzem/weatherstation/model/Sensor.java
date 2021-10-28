package com.fritzem.weatherstation.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Sensor {

    @Id
    @GeneratedValue
    private long id;

    private int temp = 65;

    protected Sensor() {} //For JPA
    public Sensor(int temp) {
        this.temp = temp;
    }

    public long getId() {
        return id;
    }

    public int getNot() {
        return 5;
    }
}
