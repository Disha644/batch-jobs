package com.linkedin.batch.jobs.chunkjob;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChunkJobConfiguration {

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public ChunkJobItemReader reader;

    @Autowired
    public ChunkJobItemWriter writer;
    
    @Bean
    public Job chunkJob(JobBuilderFactory jobBuilderFactory) {
        return jobBuilderFactory.get("chunkJob")
            .start(chunkBasedStep()).build();
    }

    @Bean
    public Step chunkBasedStep() {
        return this.stepBuilderFactory.get("chunkBasedStep")
            .<String, String>chunk(3)
            .reader(reader)
            .writer(writer)
            .build();
    }
}
