package batch.com.csvtopdfgenerater.processor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import batch.com.csvtopdfgenerater.entity.PatientReport;

@Component
public class ReportFinder implements ItemProcessor<PatientReport, PatientReport> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportFinder.class);

    private final JdbcTemplate jdbcTemplate;
    private final String FILTER_ID = "123";

    @Autowired
    public ReportFinder(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public PatientReport process(PatientReport patientReport) throws Exception {
        LOGGER.info("Processing patient report: {}", patientReport);

        String sql = "SELECT report_id FROM patient_report WHERE report_id = ?";
        @SuppressWarnings("deprecation")
		List<String> results = jdbcTemplate.query(sql, new Object[]{FILTER_ID}, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("report_id");
            }
        });

        if (!results.isEmpty() && results.contains(patientReport.getReportId())) {
            LOGGER.info("PatientReport {} matches filter criteria.", patientReport);
            return patientReport;
        } else {
            LOGGER.info("PatientReport {} does not match filter criteria. Skipping.", patientReport);
            return null;
        }
    }
}
