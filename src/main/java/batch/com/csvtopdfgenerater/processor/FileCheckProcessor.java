package batch.com.csvtopdfgenerater.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import batch.com.csvtopdfgenerater.entity.PatientReport;
@Component
public class FileCheckProcessor implements ItemProcessor<PatientReport, PatientReport> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileCheckProcessor.class);
    private static final String FILTER_ID = "0f201a7b-722b-452c-a1c9-51b81b071699";

    @Override
    public PatientReport process(PatientReport person) throws Exception {
        LOGGER.info("Processing person: {}", person);

        if (FILTER_ID.equals(person.getPatientId())) {
            LOGGER.info("PatientReport {} matches filter criteria.", person);
            return person;
        } else {
            LOGGER.info("PatientReport {} does not match filter criteria. Skipping.", person);
            return null;
        }
    }
}
