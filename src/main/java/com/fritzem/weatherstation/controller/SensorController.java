package com.fritzem.weatherstation.controller;

import com.fritzem.weatherstation.model.Report;
import com.fritzem.weatherstation.repository.ReportRepository;
import com.fritzem.weatherstation.repository.SensorRepository;
import com.fritzem.weatherstation.model.Sensor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class SensorController {

    final SensorRepository sensorRepository;
    final ReportRepository reportRepository;

    public SensorController(SensorRepository sensorRepository, ReportRepository reportRepository) {
        this.sensorRepository = sensorRepository;
        this.reportRepository = reportRepository;
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

    @GetMapping("/{id}")
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

    @PostMapping("/{id}/report")
    public Report report(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        Report report = new Report(
            sensorRepository.findById(Long.parseLong(id)),
            Double.parseDouble(body.get("temperature")),
            Double.parseDouble(body.get("humidity"))
        );
        reportRepository.save(report);
        return report;
    }
}
