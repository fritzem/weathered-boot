package com.fritzem.weatherstation.controller;

import com.fritzem.weatherstation.SensorRepository;
import com.fritzem.weatherstation.model.Sensor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class SensorController {

    final SensorRepository sensorRepository;

    public SensorController(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @RequestMapping
    public String about() {
        return "This is the weather station. Beep boop.";
    }

    /**
    @GetMapping("/temp")
    public Sensor sensor(@RequestParam(value = "temp", defaultValue = "") String temp) {
        return new Sensor(Integer.parseInt(temp));
    } */

    @GetMapping("/sensor/{id}")
    public Sensor checkSensor(@PathVariable String id) {
        return sensorRepository.findById(Long.parseLong(id));
    }

    @PostMapping("/create") //RequestBody for raw json, ModelAttribute for form data
    public Sensor create(@RequestBody Map<String, String> body) {
        Sensor sensor = new Sensor(
            body.get("country"),
            body.get("city")
        );
        sensorRepository.save(sensor);
        return sensor;
    }
}
