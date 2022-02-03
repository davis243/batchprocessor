package com.davis.batchprocessor.configuration;

import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class JobLauncherConfig {

    @Bean
    public JobLauncher jobLauncher1(ThreadPoolTaskExecutor taskExecutor, JobRepository jobRepository){
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setTaskExecutor(taskExecutorJobLauncher());
        jobLauncher.setJobRepository(jobRepository);
        return jobLauncher;
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutorJobLauncher() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(1);
        taskExecutor.setMaxPoolSize(1);
        taskExecutor.setQueueCapacity(2);
        return taskExecutor;
    }
}
