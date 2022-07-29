package com.linkedin.batch.jobs.prepareflower;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.linkedin.batch.listeners.FlowerSelectionStepExecutionListener;

@Configuration
public class PrepareFlowerJobConfiguration {
    
    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public FlowerSelectionStepExecutionListener listener;

    @Autowired
	@Qualifier("deliveryFlow")
	public Flow deliveryFlow;

    @Bean
    @Qualifier("prepareFlower")
    public Job prepareflower(JobBuilderFactory jobBuilderFactory) {
        return jobBuilderFactory.get("prepareFlower")
            .start(selectFlowerStep())
                .on("TRIM_REQUIRED").to(removeThornsStep()).next(arrangeFlowerStep())
            .from(selectFlowerStep())
                .on("TRIM_NOT_REQUIRED").to(arrangeFlowerStep())
            .from(arrangeFlowerStep())
                .on("*").to(deliveryFlow)
            .end()
            .build();
    }

    @Bean
    public Step selectFlowerStep() {
        return this.stepBuilderFactory.get("selectFlowerStep").tasklet(new Tasklet() {

            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("Selecting flower from the garden.");
               return RepeatStatus.FINISHED;
            }
            
        }).listener(listener).build();
    }

    @Bean
    public Step removeThornsStep() {
        return this.stepBuilderFactory.get("removeThornsStep").tasklet(new Tasklet() {

            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("Removing thorns from the flower.");
               return RepeatStatus.FINISHED;
            }
            
        }).build();
    }

    @Bean
    public Step arrangeFlowerStep() {
        return this.stepBuilderFactory.get("arrangeFlowerStep").tasklet(new Tasklet() {

            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("Arranging the flower in the pot.");
               return RepeatStatus.FINISHED;
            }
            
        }).build();
    }


}
