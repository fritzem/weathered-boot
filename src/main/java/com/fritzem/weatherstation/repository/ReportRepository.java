package com.fritzem.weatherstation.repository;

import com.fritzem.weatherstation.model.Report;
import com.fritzem.weatherstation.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Report findBySensor(Sensor s);

}
