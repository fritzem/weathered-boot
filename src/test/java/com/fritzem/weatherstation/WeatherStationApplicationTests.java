package com.fritzem.weatherstation;

import com.fritzem.weatherstation.model.Report;
import com.fritzem.weatherstation.model.Sensor;
import com.fritzem.weatherstation.repository.ReportRepository;
import com.fritzem.weatherstation.repository.SensorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ActiveProfiles("test")
class WeatherStationApplicationTests {

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    SensorRepository sensorRepository;

    private Sensor testSensor;
    private Report testReport;

    @BeforeEach
    public void setup() {
        testSensor = new Sensor("testland", "testtown");
        sensorRepository.save(testSensor);
    }

    @AfterEach
    public void teardown() {
        sensorRepository.deleteAll();
        reportRepository.deleteAll();
    }

    @Test
    void createAndRetrieveSensor() {
        Sensor sensor = new Sensor(any(), any());
        sensorRepository.save(sensor);
        assertNotNull(sensorRepository.findById(sensor.getId()));
    }

    @Test
    void createAndRetrieveReport() {
        Report report = new Report(testSensor, any(), any(), any(), any());
        assertNotNull(reportRepository.findById(report.getId()));
    }

}
