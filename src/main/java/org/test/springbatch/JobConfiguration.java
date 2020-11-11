package org.test.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.item.support.PassThroughItemProcessor;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Configuration
@EnableBatchProcessing
class JobConfiguration {

    @Bean
    Job testJob(JobBuilderFactory jobBuilderFactory, Step testStep) {
        return jobBuilderFactory.get("testJob")
                .start(testStep)
                .build();
    }

    @Bean
    Step testStep(StepBuilderFactory stepBuilderFactory, ItemWriter<JAXBItem> writer) {
        return stepBuilderFactory.get("testStep")
                .<JAXBItem, JAXBItem>chunk(10)
                .reader(new ListItemReader<>(Arrays.asList(new JAXBItem(), new JAXBItem())))
                .processor(new PassThroughItemProcessor<>())
                .writer(writer)
                .build();
    }

    @Bean
    StaxEventItemWriter<JAXBItem> writer() throws IOException {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(JAXBItem.class);
        Path tempFile = Files.createTempFile("test", ".xml");
        return new StaxEventItemWriterBuilder<JAXBItem>()
                .name("fileWriter")
                .resource(new FileSystemResource(tempFile))
                .rootTagName("test")
                .marshaller(marshaller)
                .build();
    }

    @XmlRootElement(name = "item", namespace = "https://www.example.com/test")
    private static class JAXBItem {
    }

}
