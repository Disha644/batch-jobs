package com.linkedin.batch.jobs.chunkjob;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class ChunkJobItemWriter  implements ItemWriter<String> {

    @Override
    public void write(List<? extends String> items) throws Exception {
        System.out.println("Recived list of size: " + items.size());
        items.forEach(System.out::println);
    }
    
}
