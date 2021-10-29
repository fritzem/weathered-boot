package com.fritzem.weatherstation.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fritzem.weatherstation.model.Report;
import com.fritzem.weatherstation.repository.ReportRepository;
import com.fritzem.weatherstation.repository.SensorRepository;
import com.fritzem.weatherstation.model.Sensor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class SensorController {

    final SensorRepository sensorRepository;
    final ReportRepository reportRepository;

    public SensorController(SensorRepository sensorRepository, ReportRepository reportRepository) {
        this.sensorRepository = sensorRepository;
        this.reportRepository = reportRepository;
    }

    /*
     * Generic message for base url. Maybe make this a help function.
     */
    @RequestMapping
    public String about() {
        return "This is the weather station. Beep boop.";
    }

    /*
     * Returns a sensor based on id
     */
    @GetMapping("/{id}")
    public Sensor checkSensor(@PathVariable String id) {
        return sensorRepository.findById(Long.parseLong(id));
    }

    /*
     * Creates a sensor
     * @param country
     * @param city
     */
    @PostMapping("/create") //RequestBody for raw json, ModelAttribute for form data
    public Sensor create(@RequestBody Map<String, String> body) {
        Sensor sensor = new Sensor(
            body.get("country"),
            body.get("city")
        );
        sensorRepository.save(sensor);
        return sensor;
    }

    /*
     * Generates a report for a sensor
     * @param temperature
     * @param humidity
     */
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

    /*
     * Queries reports
     * @param sensorIds ~ list of sensor ids to include, leave blank to query all sensors
     */
    @PostMapping("/reports")
    public List<Sensor> report(
            @RequestBody JsonNode payload) {

        List<Sensor> sensors;

        ArrayList<Long> sensorIds = new ArrayList<>();

        JsonNode payloadIds = payload.get("sensorIds");
        if (payloadIds != null && payloadIds.isArray() && !payloadIds.isEmpty()) {
            for (JsonNode jn : payloadIds) {
                sensorIds.add(Long.parseLong(jn.toString()));
            }
            sensors = sensorRepository.findAllById(sensorIds);
        } else {
            sensors = sensorRepository.findAll();
        }

        return sensors;





    }
}
