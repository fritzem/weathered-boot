package com.fritzem.weatherstation;

import com.fritzem.weatherstation.model.Sensor;
import org.springframework.data.repository.CrudRepository;

public interface SensorRepository extends CrudRepository<Sensor, Long> {

    Sensor findById(long id);
}
