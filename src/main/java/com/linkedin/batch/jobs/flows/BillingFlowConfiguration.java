package com.linkedin.batch.jobs.flows;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BillingFlowConfiguration {
    
    @Autowired
	public StepBuilderFactory stepBuilderFactory;;
    
    @Bean
	@Qualifier("billingFlow")
    public Flow billingFlow() {
        return new FlowBuilder<SimpleFlow>("billingFlow").start(sendInvoiceStep()).build();
    }

    @Bean
	public Step sendInvoiceStep() {
		return this.stepBuilderFactory.get("sendInvoiceStep").tasklet(new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println("Invoice sent to customer sucessfully.");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
}
