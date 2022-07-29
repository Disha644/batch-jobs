package com.linkedin.batch.deciders;

import java.time.LocalDateTime;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;

@Component
public class DeliveryDecider implements JobExecutionDecider {

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        String status = LocalDateTime.now().getHour() < 12 ? "PRESENT" : "NOT_PRESENT";
        System.out.println(String.format("The customer is %s at home", status));
        return new FlowExecutionStatus(status);
    }
    
}
