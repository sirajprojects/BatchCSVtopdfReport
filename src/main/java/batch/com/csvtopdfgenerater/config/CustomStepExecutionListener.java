package batch.com.csvtopdfgenerater.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomStepExecutionListener implements StepExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomStepExecutionListener.class);

    @Autowired
    private JobRepository jobRepository;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        LOGGER.info("Starting step: {}", stepExecution.getStepName());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (stepExecution.getStatus() == BatchStatus.FAILED) {
            LOGGER.error("Step failed: {}. Errors: {}", stepExecution.getStepName(), stepExecution.getFailureExceptions());
        }
        return stepExecution.getExitStatus();
    }
}
