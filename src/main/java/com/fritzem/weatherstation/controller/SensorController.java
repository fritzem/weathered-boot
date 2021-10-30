package com.fritzem.weatherstation.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fritzem.weatherstation.model.Report;
import com.fritzem.weatherstation.repository.ReportRepository;
import com.fritzem.weatherstation.repository.SensorRepository;
import com.fritzem.weatherstation.model.Sensor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;
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
    public ResponseEntity<Object> report(
            @RequestBody JsonNode payload) {

        Map<String, Object> response = new HashMap<>();

        List<Sensor> sensors;
        JsonNode payloadIds = payload.get("sensorIds");
        if (payloadIds != null && payloadIds.isArray() && !payloadIds.isEmpty()) {
            ArrayList<Long> sensorIds = new ArrayList<>();
            for (JsonNode jn : payloadIds) {
                sensorIds.add(Long.parseLong(jn.toString()));
            }
            sensors = sensorRepository.findAllById(sensorIds);
        } else {
            sensors = sensorRepository.findAll();
        }

        JsonNode from = payload.get("from");
        JsonNode to   = payload.get("to");

        ArrayList<Object> reports = new ArrayList<>();
        for (Sensor s : sensors) {

            List<Report> sensorReports;
            if (from != null && to != null) {
                sensorReports = reportRepository.findAllBySensorAndTimeBetween(s,
                        Timestamp.valueOf(from.textValue()),
                        Timestamp.valueOf(to.textValue()));
            } else if (from != null) {
                sensorReports = reportRepository.findAllBySensorAndTimeGreaterThanEqual(s,
                        Timestamp.valueOf(from.textValue()));
            } else if (to != null) {
                sensorReports = reportRepository.findAllBySensorAndTimeLessThanEqual(s,
                        Timestamp.valueOf(to.textValue()));
            } else {
                //Query all reports
                sensorReports = reportRepository.findAllBySensor(s);
            }

            if (!sensorReports.isEmpty()) {
                Map<String, Object> sensorReport = new HashMap<>();
                sensorReport.put("averageTemperature", reportRepository.averageTemperature(sensorReports));
                sensorReport.put("averageHumidity", reportRepository.averageHumidity(sensorReports));
                sensorReport.put("Sensor", s);
                reports.add(sensorReport);
            }
        }
        response.put("Reports", reports);


        return new ResponseEntity<>(response, HttpStatus.OK);

    //        return new ResponseEntity<String>(sensors, HttpStatus.OK);

    }

    @RequestMapping("/testQuery")
    public Map<String, Object> test() {
        List<Long> list = Arrays.asList(3L);
        return sensorRepository.testQuery(list);
    }

}
