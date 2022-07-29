package com.linkedin.batch.deciders;

import java.util.Random;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;

@Component
public class RefundDecider implements JobExecutionDecider {

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        String status = new Random().nextFloat() < 0.70f ? "INCORRECT_ITEM" : "CORRECT_ITEM";
        System.out.println("The item recived to the customer is " + status);
        return new FlowExecutionStatus(status);
    }
    
}
