package com.fritzem.weatherstation;

public class Sensor {

    private final int id;
    private final int temp = 65;

    public Sensor(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getNot() {
        return 5;
    }
}
