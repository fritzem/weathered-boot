package com.fritzem.weatherstation;

import com.fritzem.weatherstation.repository.SensorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WeatherStationApplication {

    private static final Logger log =
            LoggerFactory.getLogger(WeatherStationApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(WeatherStationApplication.class, args);
    }

    @Bean
    public CommandLineRunner sensorInit(SensorRepository repo) {
        return (args) -> {
            //repo.save(new Sensor(663));
            //repo.save(new Sensor(23));

            //log.info("HEY");
            //log.info(repo.findById(1L).toString());
        };
    }

}
