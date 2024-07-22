package batch.com.csvtopdfgenerater.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import batch.com.csvtopdfgenerater.entity.PatientReport;

public interface ReportRepository extends JpaRepository<PatientReport, Long> {
}