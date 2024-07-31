package batch.com.csvtopdfgenerater.config;


**Batch Configuration Class:**

```java
package com.example.demo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.tasklets.ClassATasklet;
import com.example.demo.tasklets.ClassBTasklet;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public BatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .next(step2())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(new ClassATasklet())
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet(new ClassBTasklet())
                .build();
    }
}
```

**ClassATasklet Class:**

```java
package com.example.demo.tasklets;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class ClassATasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        // Logic from ClassA's main method
        System.out.println("Running ClassA main method logic");
        ClassA.main(new String[]{});
        return RepeatStatus.FINISHED;
    }
}
```

**ClassBTasklet Class:**

```java
package com.example.demo.tasklets;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class ClassBTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        // Logic from ClassB's main method
        System.out.println("Running ClassB main method logic");
        ClassB.main(new String[]{});
        return RepeatStatus.FINISHED;
    }
}
```

**ClassA:**

```java
package com.example.demo;

public class ClassA {
    public static void main(String[] args) {
        // Original logic from ClassA's main method
        System.out.println("Executing ClassA main method");
    }
}
```

**ClassB:**

```java
package com.example.demo;

public class ClassB {
    public static void main(String[] args) {
        // Original logic from ClassB's main method
        System.out.println("Executing ClassB main method");
    }
}
```
