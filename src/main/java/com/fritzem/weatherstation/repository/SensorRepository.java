package com.fritzem.weatherstation.repository;

import com.fritzem.weatherstation.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface SensorRepository extends JpaRepository<Sensor, Long> {

    Sensor findById(long id);
}
