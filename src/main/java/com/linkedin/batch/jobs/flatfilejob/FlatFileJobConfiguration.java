package com.linkedin.batch.jobs.flatfilejob;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.linkedin.batch.model.Order;

@Configuration
public class FlatFileJobConfiguration {

    public static String[] tokens = new String[] {"order_id", "first_name", "last_name", "email", "cost", "item_id", "item_name", "ship_date"};
    
    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public FlatFileJobItemWriter writer;

    @Bean
    public ItemReader<Order> itemReader() {
        FlatFileItemReader<Order> itemReader = new FlatFileItemReader<>();
        itemReader.setLinesToSkip(1);
        itemReader.setResource(new FileSystemResource("/Users/gdisha/data/shipped_orders.csv"));

        DefaultLineMapper<Order> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(tokens);
    
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new OrderFieldSetMapper());
        
        itemReader.setLineMapper(lineMapper);
        return itemReader;
    }
    
    @Bean
    public Job chunkJob(JobBuilderFactory jobBuilderFactory) {
        return jobBuilderFactory.get("chunkJob")
            .start(chunkBasedStep()).build();
    }

    @Bean
    public Step chunkBasedStep() {
        return this.stepBuilderFactory.get("chunkBasedStep")
            .<Order, Order>chunk(3)
            .reader(itemReader())
            .writer(writer)
            .build();
    }
}
