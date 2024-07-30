package batch.com.csvtopdfgenerater.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Service
public class ExampleService {

    private final JdbcTemplate dataRwJdbcTemplate;
    private final JdbcTemplate logJdbcTemplate;
    private final JdbcTemplate oracleRwJdbcTemplate;
    private final JdbcTemplate oracleLogJdbcTemplate;

    public ExampleService(
            @Qualifier("dataRwJdbcTemplate") JdbcTemplate dataRwJdbcTemplate,
            @Qualifier("logJdbcTemplate") JdbcTemplate logJdbcTemplate,
            @Qualifier("oracleRwJdbcTemplate") JdbcTemplate oracleRwJdbcTemplate,
            @Qualifier("oracleLogJdbcTemplate") JdbcTemplate oracleLogJdbcTemplate) {
        this.dataRwJdbcTemplate = dataRwJdbcTemplate;
        this.logJdbcTemplate = logJdbcTemplate;
        this.oracleRwJdbcTemplate = oracleRwJdbcTemplate;
        this.oracleLogJdbcTemplate = oracleLogJdbcTemplate;
    }

    @PostConstruct
    public void checkConnections() {
        checkConnection(dataRwJdbcTemplate, "data_rw");
        checkConnection(logJdbcTemplate, "log");
        checkConnection(oracleRwJdbcTemplate, "oracle_rw");
        checkConnection(oracleLogJdbcTemplate, "oracle_log");
    }

    private void checkConnection(JdbcTemplate jdbcTemplate, String dataSourceName) {
        try {
            String sql = "SELECT 1 FROM DUAL"; // Use a simple query that works on both SQL Server and Oracle
            jdbcTemplate.queryForObject(sql, Integer.class);
            System.out.println("Connection to " + dataSourceName + " database successful.");
        } catch (Exception e) {
            System.err.println("Connection to " + dataSourceName + " database failed: " + e.getMessage());
        }
    }
}

