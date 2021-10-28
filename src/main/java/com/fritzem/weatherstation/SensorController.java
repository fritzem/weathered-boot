package com.fritzem.weatherstation;

import com.fritzem.weatherstation.model.Sensor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/temp")
    public Sensor sensor(@RequestParam(value = "temp", defaultValue = "") String temp) {
        return new Sensor(Integer.parseInt(temp));
    }

    @GetMapping("/checkSensor")
    public Sensor checkSensor(@RequestParam(value = "id", defaultValue = "0") String id) {
        return sensorRepository.findById(Long.parseLong(id));
    }
}
