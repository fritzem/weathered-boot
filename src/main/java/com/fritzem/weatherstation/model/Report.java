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

    private Double temperature;
    private Double humidity;
    private Double windSpeed;
    private Double atmPressure;

    protected Report() {}

    public Report(Sensor sensor, Double temperature, Double humidity, Double windSpeed, Double atmPressure) {
        this.sensor = sensor;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.atmPressure = atmPressure;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public Timestamp getTime() {
        return time;
    }

    public Double getTemperature() { return temperature; }
    public Double getHumidity() { return humidity; }
    public Double getWindSpeed() { return windSpeed; }
    public Double getAtmPressure() { return atmPressure; }

}
