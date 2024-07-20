package batch.com.csvtopdfgenerater.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import batch.com.csvtopdfgenerater.entity.PatientReport;

@Component
public class BatchFileReader {

	private static final Logger LOGGER = LoggerFactory.getLogger(BatchFileReader.class);

	@Bean
	public FlatFileItemReader<PatientReport> reader() {
		LOGGER.info("Starting to read CSV file.");

		FlatFileItemReader<PatientReport> reader = new FlatFileItemReader<>();
		reader.setLinesToSkip(1);
		reader.setResource(new ClassPathResource("report_sample.csv"));
		reader.setLineMapper(new DefaultLineMapper<PatientReport>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
		                setNames("patient_id", "patient_name", "date_of_birth", "gender", "report_id", "date_of_report", "specimen_type", "specimen_collection_date", "diagnosis", "microscopic_description", "pathologist_name", "comments");
		            }
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<PatientReport>() {
					{
						setTargetType(PatientReport.class);
					}
				});
			}
		});

		LOGGER.info("Finished setting up CSV reader.");
		return reader;
	}

}
