package com.fritzem.weatherstation.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Sensor {

    @Id
    @GeneratedValue
    private long id;

    private String country;
    private String city;


    protected Sensor() {} //For JPA
    public Sensor(String country, String city) {
        this.country = country;
        this.city = city;
    }

    public long getId() {
        return id;
    }
    public String getCountry() {
        return country;
    }
    public String getCity()  { return city; }


}
