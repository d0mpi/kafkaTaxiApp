package com.epam.kafka.training.config;

import com.epam.kafka.training.model.VehicleInfo;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.DoubleDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    @Value("${kafka.bootstrapServer}")
    private String bootstrapServer;
    @Value("${kafka.DQLPostfix}")
    private String dqlPostfix;

    private final KafkaTemplate<String, VehicleInfo> inputTemplate;
    private final KafkaTemplate<String, Double> outputTemplate;

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, VehicleInfo>>
    inputListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, VehicleInfo> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(inputConsumerFactory());
        factory.setConcurrency(3);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(inputTemplate,
                (r, e) -> new TopicPartition(r.topic() + dqlPostfix, r.partition()));
        CommonErrorHandler errorHandler = new DefaultErrorHandler(recoverer, new FixedBackOff(0L, 2L));
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, VehicleInfo> inputConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.epam.kafka.training.model");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>());
    }

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Double>>
    outputListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Double> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(outputConsumerFactory());
        factory.setConcurrency(1);
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(outputTemplate,
                (r, e) -> new TopicPartition(r.topic() + dqlPostfix, r.partition()));
        CommonErrorHandler errorHandler = new DefaultErrorHandler(recoverer, new FixedBackOff(0L, 2L));
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, Double> outputConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, DoubleDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.epam.kafka.training.model");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new DoubleDeserializer());
    }
}
