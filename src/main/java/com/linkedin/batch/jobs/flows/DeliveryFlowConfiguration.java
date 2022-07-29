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

import com.linkedin.batch.deciders.DeliveryDecider;
import com.linkedin.batch.deciders.RefundDecider;

@Configuration
public class DeliveryFlowConfiguration {

    @Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public DeliveryDecider deliveryDecider;
	
	@Autowired
	public RefundDecider refundDecider;
    
    @Bean
	@Qualifier("deliveryFlow")
    public Flow deliveryFlow() {
        return new FlowBuilder<SimpleFlow>("deliveryFlow").start(driveToAddressStep())
				.on("FAILED").fail()
			.from(driveToAddressStep())
				.on("*").to(deliveryDecider)
					.on("PRESENT").to(giveItemToCustomerStep())
						.next(refundDecider)
							.on("CORRECT_ITEM").to(thankCustomerStep())
						.from(refundDecider)
							.on("INCORRECT_ITEM").to(initiateRefundStep())
				.from(deliveryDecider)
					.on("NOT_PRESENT").to(leaveAtDoorStep())
            .build();
    }

    @Bean
	public Step driveToAddressStep() {
		boolean GOT_LOST = false;
		return this.stepBuilderFactory.get("driveToAddressStep").tasklet(new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				if(GOT_LOST) {
					throw new RuntimeException("Got Lost while driving to the address");
				}

				System.out.println("Arrvied to the address sucessfully!");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}

	@Bean
	public Step giveItemToCustomerStep() {
		return this.stepBuilderFactory.get("giveItemToCustomerStep").tasklet(new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				String address = chunkContext.getStepContext().getJobParameters().get("address").toString();
				System.out.println(String.format("Item delivered successfully at %s", address));
				return RepeatStatus.FINISHED;
			}
		}).build();
	}

	@Bean
	public Step thankCustomerStep() {
		return this.stepBuilderFactory.get("thankCustomerStep").tasklet(new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println("Thank you for ordering from us.");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}

	@Bean
	public Step initiateRefundStep() {
		return this.stepBuilderFactory.get("initiateRefundStep").tasklet(new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println("Refund initiated successfully");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}

	@Bean
	public Step leaveAtDoorStep() {
		return this.stepBuilderFactory.get("leaveAtDoorStep").tasklet(new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println("Left the package at the door.");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}

	@Bean
	public Step storePackageStep() {
		return this.stepBuilderFactory.get("storePackageStep").tasklet(new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println("Got lost while delivering the package.");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
    
}
