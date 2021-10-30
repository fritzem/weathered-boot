package com.fritzem.weatherstation.repository;

import com.fritzem.weatherstation.model.Report;
import com.fritzem.weatherstation.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findAllBySensor(Sensor s);

    @Query(
            "SELECT avg(r.temperature) FROM Report r WHERE r in :reports"
    )
    Double averageTemperature(List<Report> reports);

    @Query(
            "SELECT avg(r.humidity) FROM Report r WHERE r in :reports"
    )
    Double averageHumidity(List<Report> reports);
}
