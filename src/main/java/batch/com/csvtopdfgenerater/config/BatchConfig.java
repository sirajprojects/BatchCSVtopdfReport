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
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import batch.com.csvtopdfgenerater.entity.PatientReport;
import batch.com.csvtopdfgenerater.processor.FileCheckProcessor;
import batch.com.csvtopdfgenerater.processor.ReportFinder;
import batch.com.csvtopdfgenerater.reader.BatchFileReader;
import batch.com.csvtopdfgenerater.writer.DataWriter;
import batch.com.csvtopdfgenerater.writer.PdfItemWriter;

@Configuration
@EnableBatchProcessing
@EnableTransactionManagement
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

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Bean
	public Job importUserJob() {
		LOGGER.info("Setting up job.");
		return jobBuilderFactory.get("importUserJob").incrementer(new RunIdIncrementer()).flow(step1()).next(step2())
				.end().build();
	}

	@Bean
	public Step step1() {
		LOGGER.info("Setting up step1.");
		return stepBuilderFactory.get("csvtomysql").<PatientReport, PatientReport>chunk(1000).reader(reader.reader())
				.processor(csvprocessor).writer(writer).throttleLimit(1000).taskExecutor(taskExecutor()).build();
	}

	@Bean
	public Step step2() {
		LOGGER.info("Setting up step2.");
		return stepBuilderFactory.get("generatePdfStep").<PatientReport, PatientReport>chunk(1000)
				.reader(pagingItemReader()).processor(sqlprocessor).writer(pdfItemWriter()).throttleLimit(1000)
				.taskExecutor(taskExecutor()).transactionManager(transactionManager).build();
	}

	@Bean
	public JdbcPagingItemReader<PatientReport> pagingItemReader() {
		LOGGER.info("Initializing JdbcPagingItemReader with SQL query to fetch PatientReport data.");
		JdbcPagingItemReader<PatientReport> reader = new JdbcPagingItemReader<>();
		reader.setDataSource(dataSource);
		reader.setPageSize(1000);
		reader.setRowMapper(new BeanPropertyRowMapper<>(PatientReport.class));

		SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
		queryProvider.setDataSource(dataSource);
		queryProvider.setSelectClause(
				"SELECT patient_id, patient_name, date_of_birth, gender, report_id, date_of_report, specimen_type, specimen_collection_date, diagnosis, microscopic_description, pathologist_name, comments");
		queryProvider.setFromClause("FROM patient_report");
		queryProvider.setSortKey("patient_id");

		try {
			reader.setQueryProvider(queryProvider.getObject());
		} catch (Exception e) {
			LOGGER.error("Error initializing JdbcPagingItemReader: ", e);
			throw new IllegalStateException("Failed to initialize JdbcPagingItemReader", e);
		}
		return reader;
	}

	@Bean
	public TaskExecutor taskExecutor() {
		return new SimpleAsyncTaskExecutor() {
			@Override
			public void execute(Runnable task, long startTimeout) {
				super.execute(() -> {
					LOGGER.info("Executing with thread: " + Thread.currentThread().getName());
					task.run();
				}, startTimeout);
			}
		};
	}

	@Bean
	public PdfItemWriter pdfItemWriter() {
		LOGGER.info("Initializing PdfItemWriter.");
		try {
			return new PdfItemWriter();
		} catch (Exception e) {
			LOGGER.error("Error initializing PdfItemWriter: ", e);
			throw new IllegalStateException("Failed to initialize PdfItemWriter", e);
		}
	}
}
