package com.fritzem.weatherstation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SensorController {

    @RequestMapping
    public String about() {
        return "This is the weather station. Beep boop.";
    }

    @GetMapping("/temp")
    public Sensor sensor(@RequestParam(value = "id", defaultValue = "") String id) {
        return new Sensor(Integer.parseInt(id));
    }
}
