package com.fritzem.weatherstation.repository;

import com.fritzem.weatherstation.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface SensorRepository extends JpaRepository<Sensor, Long> {

    Sensor findById(long id);


    @Query(
            "SELECT s, r FROM Sensor s \n" +
            "INNER JOIN Report r ON s.id=r.sensor.id \n" +
            "WHERE s.id in :sensors"
    )
    Map<String, Object> testQuery(List<Long> sensors);

}
