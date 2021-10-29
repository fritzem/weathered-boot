package com.fritzem.weatherstation.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Report {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Sensor sensor; //Foreign key

    @CreationTimestamp
    private Timestamp time;

    private double temperature;
    private double humidity;

    protected Report() {}

    public Report(Sensor sensor, double temperature, double humidity) {
        this.sensor = sensor;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public Timestamp getTime() {
        return time;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

}
