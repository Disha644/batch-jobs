package com.linkedin.batch.listeners;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class FlowerSelectionStepExecutionListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("Executing before step logic");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("Executing after step logic");
        String flowerType = stepExecution.getJobParameters().getString("type");
        return flowerType.equalsIgnoreCase("rose") ? 
            new ExitStatus("TRIM_REQUIRED") : new ExitStatus("TRIM_NOT_REQUIRED");
    }
    
}
