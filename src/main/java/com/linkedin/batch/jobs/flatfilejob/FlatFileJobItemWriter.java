package com.linkedin.batch.jobs.flatfilejob;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.linkedin.batch.model.Order;

@Component
public class FlatFileJobItemWriter  implements ItemWriter<Order> {

    @Override
    public void write(List<? extends Order> items) throws Exception {
        System.out.println("Recived list of size: " + items.size());
        items.forEach(System.out::println);
    }
    
}
