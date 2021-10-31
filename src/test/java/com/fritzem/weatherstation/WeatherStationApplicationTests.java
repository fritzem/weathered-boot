package com.fritzem.weatherstation;

import com.fritzem.weatherstation.model.Report;
import com.fritzem.weatherstation.model.Sensor;
import com.fritzem.weatherstation.repository.ReportRepository;
import com.fritzem.weatherstation.repository.SensorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.params.provider.Arguments.arguments;
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
        reportRepository.deleteAll();
        sensorRepository.deleteAll();
    }

    @Test
    public void createAndRetrieveSensor() {
        Sensor sensor = new Sensor(any(), any());
        sensorRepository.save(sensor);
        assertNotNull(sensorRepository.findById(sensor.getId()));
    }

    @Test
    public void createAndRetrieveReport() {
        Report report = new Report(testSensor, any(), any(), any(), any());
        assertNotNull(reportRepository.findById(report.getId()));
    }

    //You could repeat this pattern and use the same stream for other columns
    @ParameterizedTest
    @MethodSource("valuesAndAverage")
    public void calculateAverageTemperature(Double[] input) {
        Double count = 0.0;
        for (Double temp : input) {
            reportRepository.save(new Report(testSensor, temp, any(), any(), any()));
            count += temp;
        }
        assert(count / input.length == reportRepository.averageTemperature(reportRepository.findAll()));
    }

    private static Stream<Arguments> valuesAndAverage() {
        return Stream.of(
                arguments((Object) new Double[]{1.0, 1.0, 1.0}),
                arguments((Object) new Double[]{25.0, 34.1, 90.1})
        );
    }

}
