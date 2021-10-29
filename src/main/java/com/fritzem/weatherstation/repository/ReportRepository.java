package com.fritzem.weatherstation.repository;

import com.fritzem.weatherstation.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

}
