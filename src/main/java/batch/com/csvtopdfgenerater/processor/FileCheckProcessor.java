package batch.com.csvtopdfgenerater.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import batch.com.csvtopdfgenerater.entity.PatientReport;

@Component
public class FileCheckProcessor implements ItemProcessor<PatientReport, PatientReport> {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileCheckProcessor.class);
	private static final String FILTER_ID = "123";

	@Override
	public PatientReport process(PatientReport patientReport) throws Exception {
		LOGGER.info("Processing person: {}", patientReport);

		if (FILTER_ID.equals(patientReport.getPatientId())) {
			LOGGER.info("PatientReport {} matches filter criteria.", patientReport);
			return patientReport;
		} else {
			LOGGER.info("PatientReport {} does not match filter criteria. Skipping.", patientReport);
			return null;
		}
	}

}
