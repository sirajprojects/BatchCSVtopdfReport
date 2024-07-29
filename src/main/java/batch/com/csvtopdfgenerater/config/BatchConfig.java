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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import batch.com.csvtopdfgenerater.entity.PatientReport;
import batch.com.csvtopdfgenerater.processor.FileCheckProcessor;
import batch.com.csvtopdfgenerater.processor.ReportFinder;
import batch.com.csvtopdfgenerater.reader.BatchFileReader;
import batch.com.csvtopdfgenerater.writer.DataWriter;

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

	@Autowired
	private JdbcPagingItemReaderConfig jdbcPagingItemReaderConfig;

	@Autowired
	private TaskExecutorConfig taskExecutorConfig;

	@Autowired
	private PdfItemWriterConfig pdfItemWriterConfig;

	@Autowired
	private CustomStepExecutionListener stepExecutionListener;

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
				// .processor(csvprocessor)
				.writer(writer).taskExecutor(taskExecutorConfig.taskExecutor()).listener(stepExecutionListener).build();
	}

	@Bean
	public Step step2() {
		LOGGER.info("Setting up step2.");
		return stepBuilderFactory.get("generatePdfStep").<PatientReport, PatientReport>chunk(1000)
				.reader(jdbcPagingItemReaderConfig.pagingItemReader(dataSource))
				// .processor(sqlprocessor)
				.writer(pdfItemWriterConfig.pdfItemWriter()).taskExecutor(taskExecutorConfig.taskExecutor())
				.transactionManager(transactionManager).listener(stepExecutionListener).build();
	}

}