package batch.com.csvtopdfgenerater.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import batch.com.csvtopdfgenerater.entity.PatientReport;
import batch.com.csvtopdfgenerater.processor.FileCheckProcessor;
import batch.com.csvtopdfgenerater.processor.ReportFinder;
import batch.com.csvtopdfgenerater.reader.BatchFileReader;
import batch.com.csvtopdfgenerater.writer.DataWriter;
import batch.com.csvtopdfgenerater.writer.PdfItemWriter;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(BatchConfig.class);

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	private BatchFileReader reader;

	@Autowired
	private FileCheckProcessor csvprocessor;
	
	@Autowired
	private ReportFinder sqlprocessor;

	@Autowired
	private DataWriter writer;

	@Autowired
	private DataSource dataSource;

	@Bean
	public Job importUserJob() {
		LOGGER.info("Setting up job.");

		return jobBuilderFactory.get("importUserJob").incrementer(new RunIdIncrementer()).flow(step1())
				.next(step2()).end().build();
	}

	@Bean
	public Step step1() {
		LOGGER.info("Setting up step.");

		return stepBuilderFactory.get("csvtomysql").<PatientReport, PatientReport>chunk(10000)
				.reader(reader.reader())
				//.processor(csvprocessor)
				.writer(writer)
				.build();
	}

	@Bean
	public Step step2() {
		return stepBuilderFactory.get("generatePdfStep").<PatientReport, PatientReport>chunk(10000).reader(dbReader(dataSource))
				.processor(sqlprocessor).writer(pdfItemWriter()).build();
	}

	@Bean
	public JdbcCursorItemReader<PatientReport> dbReader(DataSource dataSource) {
		LOGGER.info("Initializing JdbcCursorItemReader with SQL query to fetch PatientReport data.");
		JdbcCursorItemReader<PatientReport> reader = new JdbcCursorItemReader<>();
		reader.setDataSource(dataSource);
		reader.setSql("SELECT patient_id, patient_name, date_of_birth, gender, report_id, date_of_report, specimen_type, specimen_collection_date, diagnosis, microscopic_description, pathologist_name, comments FROM patient_report");
		reader.setRowMapper(new BeanPropertyRowMapper<>(PatientReport.class));
		LOGGER.info("JdbcCursorItemReader initialized with SQL: {}", reader.getSql());

		return reader;
	}

	@Bean
	public PdfItemWriter pdfItemWriter() {
		return new PdfItemWriter();
	}
}
