package com.epam.kafka.training.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Value("${kafka.inputTopic}")
    private String inputTopic;
    @Value("${kafka.outputTopic}")
    private String outputTopic;
    @Value("${kafka.DQLPostfix}")
    private String dqlPostfix;

    @Bean
    public NewTopic inputTopic() {
        return TopicBuilder.name(inputTopic)
                .partitions(3)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic inputTopicDLQ() {
        return TopicBuilder.name(inputTopic + dqlPostfix)
                .partitions(3)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic outputTopic() {
        return TopicBuilder.name(outputTopic)
                .partitions(3)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic outputTopicDLQ() {
        return TopicBuilder.name(outputTopic + dqlPostfix)
                .partitions(3)
                .replicas(2)
                .build();
    }

}
