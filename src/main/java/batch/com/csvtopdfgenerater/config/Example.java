package batch.com.batch.org.mybatis.batch;



import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import batch.com.batch.org.mybatis.dto.AppDao;
import batch.com.batch.org.mybatis.model.App;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final AppDao appDao;

    public BatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, AppDao appDao) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.appDao = appDao;
    }

    @Bean
    public Job exampleJob() {
        return jobBuilderFactory.get("exampleJob")
                .incrementer(new RunIdIncrementer())
                .start(exampleStep())
                .build();
    }

    @Bean
    public Step exampleStep() {
        return stepBuilderFactory.get("exampleStep")
                .tasklet(exampleTasklet())
                .build();
    }

    @Bean
    public Tasklet exampleTasklet() {
        return (contribution, chunkContext) -> {
            List<App> apps = appDao.getAllApps();
            apps.forEach(app -> System.out.println(app.getName()));
            return RepeatStatus.FINISHED;
        };
    }
}
