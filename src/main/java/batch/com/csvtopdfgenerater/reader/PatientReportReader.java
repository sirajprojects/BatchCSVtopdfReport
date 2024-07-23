package batch.com.csvtopdfgenerater.reader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.batch.item.ItemReader;

import batch.com.csvtopdfgenerater.entity.PatientReport;
import batch.com.csvtopdfgenerater.repository.ReportRepository;

public class PatientReportReader implements ItemReader<List<PatientReport>> {
    private final ReportRepository reportRepository;
    private final List<PatientReport> allReports;
    private Map<String, List<PatientReport>> reportsByPatientId;
    private List<String> patientIds;
    private int currentPatientIndex = 0;

    public PatientReportReader(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
        this.allReports = reportRepository.findAll();
        this.reportsByPatientId = allReports.stream().collect(Collectors.groupingBy(PatientReport::getPatientId));
        this.patientIds = new ArrayList<>(reportsByPatientId.keySet());
    }

    @Override
    public List<PatientReport> read() {
        if (currentPatientIndex < patientIds.size()) {
            String patientId = patientIds.get(currentPatientIndex++);
            return reportsByPatientId.get(patientId);
        } else {
            return null;
        }
    }
}

