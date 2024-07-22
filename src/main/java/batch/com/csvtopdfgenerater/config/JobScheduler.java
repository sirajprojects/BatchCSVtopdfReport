package batch.com.csvtopdfgenerater.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class JobScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobScheduler.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importUserJob;

    @Scheduled(cron = "0 * * * * *")  // Every minute
    public void runScheduledJob() {
        LOGGER.info("Launching job.");
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            JobExecution jobExecution = jobLauncher.run(importUserJob, jobParameters);
            LOGGER.info("Job launched with status: {}", jobExecution.getStatus());
        } catch (Exception e) {
            LOGGER.error("Error launching job: ", e);
            throw new RuntimeException("Failed to launch job", e);
        }
    }
}
