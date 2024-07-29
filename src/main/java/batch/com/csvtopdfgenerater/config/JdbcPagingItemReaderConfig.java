package batch.com.csvtopdfgenerater.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import batch.com.csvtopdfgenerater.entity.PatientReport;

@Configuration
public class JdbcPagingItemReaderConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(JdbcPagingItemReaderConfig.class);

	@Bean
	public JdbcPagingItemReader<PatientReport> pagingItemReader(DataSource dataSource) {
		LOGGER.info("Initializing JdbcPagingItemReader with SQL query to fetch PatientReport data.");
		JdbcPagingItemReader<PatientReport> reader = new JdbcPagingItemReader<>();
		reader.setDataSource(dataSource);
		reader.setPageSize(1000);
		reader.setRowMapper(new BeanPropertyRowMapper<>(PatientReport.class));
		String sql = "SELECT patient_id, patient_name, date_of_birth, gender, report_id, date_of_report, specimen_type, specimen_collection_date, diagnosis, microscopic_description, pathologist_name, comments";
		SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
		queryProvider.setDataSource(dataSource);
		queryProvider.setSelectClause(sql);
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
}
