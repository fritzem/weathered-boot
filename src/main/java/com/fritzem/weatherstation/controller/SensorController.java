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

    Double parseHelper(String n) {
        return n == null ? null : Double.parseDouble(n); }

    /*
     * Generates a report for a sensor
     * @param temperature : double
     * @param humidity : double
     * @param windSpeed : double
     * @param atmPressure : double
     */
    @PostMapping("/{id}/report")
    public Report report(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        Report report = new Report(
            sensorRepository.findById(Long.parseLong(id)),
            parseHelper(body.get("temperature")),
            parseHelper(body.get("humidity")),
            parseHelper(body.get("windSpeed")),
            parseHelper(body.get("atmPressure"))
        );
        reportRepository.save(report);
        return report;
    }

    /*
     * Queries reports
     * @param sensorIds ~ list of sensor ids to include, leave blank to query all sensors
     * @param from : Timestamp ~ filter out reports from before this timestamp
     * @param to : Timestamp ~ filter out reports from after this timestamp
     * Not sending either timestamp queries the most recent report
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

        Timestamp from = null;
        Timestamp to = null;
        try {
            if (payload.get("from") != null) from = Timestamp.valueOf(payload.get("from").textValue());
            if (payload.get("to") != null) to = Timestamp.valueOf(payload.get("to").textValue());
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Timestamp not provided in proper format yyyy-mm-dd hh:mm:ss.[fff...] \n" + e,
                    HttpStatus.BAD_REQUEST);
        }

        ArrayList<Object> reports = new ArrayList<>();
        for (Sensor s : sensors) {
            List<Report> sensorReports;

            if (from != null && to != null) {
                sensorReports = reportRepository.findAllBySensorAndTimeBetween(s, from, to);
            } else if (from != null) {
                sensorReports = reportRepository.findAllBySensorAndTimeGreaterThanEqual(s, from);
            } else if (to != null) {
                sensorReports = reportRepository.findAllBySensorAndTimeLessThanEqual(s, to);
            } else {
                //Query most recent report
                sensorReports = reportRepository.findBySensorOrderByTimeDesc(s);
                if (!sensorReports.isEmpty()) {
                    reports.add(sensorReports.get(0));
                }
                continue;
            }

            if (!sensorReports.isEmpty()) {
                Map<String, Object> sensorReport = new HashMap<>();
                sensorReport.put("Sensor", s);

                JsonNode metrics = payload.get("metrics");
                if (metrics == null || !payloadIds.isArray() || payloadIds.isEmpty()) {
                    sensorReport.put("averageTemperature", reportRepository.averageTemperature(sensorReports));
                    sensorReport.put("averageHumidity", reportRepository.averageHumidity(sensorReports));
                    sensorReport.put("averageWindSpeed", reportRepository.averageWindSpeed(sensorReports));
                    sensorReport.put("averageAtmPressure", reportRepository.averageAtmPressure(sensorReports));
                } else {
                    for (JsonNode jn : metrics) {
                        switch (jn.asText().toUpperCase()) {
                            case "TEMPERATURE":
                                sensorReport.put("averageTemperature", reportRepository.averageTemperature(sensorReports));
                                break;
                            case "HUMIDITY":
                                sensorReport.put("averageHumidity", reportRepository.averageHumidity(sensorReports));
                                break;
                            case "WINDSPEED":
                                sensorReport.put("averageWindSpeed", reportRepository.averageWindSpeed(sensorReports));
                                break;
                            case "ATMPRESSURE":
                                sensorReport.put("averageAtmPressure", reportRepository.averageAtmPressure(sensorReports));
                                break;
                        }
                    }
                }
                reports.add(sensorReport);
            }
        }

        if (reports.isEmpty())
            return new ResponseEntity<>("No reports found.", HttpStatus.OK);
        response.put("Reports", reports);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping("/testQuery")
    public Map<String, Object> test() {
        List<Long> list = Arrays.asList(3L);
        return sensorRepository.testQuery(list);
    }

}
