package com.epam.kafka.training.config;

import com.epam.kafka.training.model.VehicleInfo;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.DoubleSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${kafka.bootstrapServer}")
    private String bootstrapServer;

    @Bean
    public KafkaTemplate<String, VehicleInfo> inputKafkaTemplate() {
        return new KafkaTemplate<>(inputProducerFactory());
    }

    @Bean
    public ProducerFactory<String, VehicleInfo> inputProducerFactory() {
        return new DefaultKafkaProducerFactory<>(inputProducerConfigs());
    }

    @Bean
    public Map<String, Object> inputProducerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        return props;
    }

    @Bean
    public KafkaTemplate<String, Double> outputKafkaTemplate() {
        return new KafkaTemplate<>(outputProducerFactory());
    }


    @Bean
    public ProducerFactory<String, Double> outputProducerFactory() {
        return new DefaultKafkaProducerFactory<>(outputProducerConfigs());
    }

    @Bean
    public Map<String, Object> outputProducerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, DoubleSerializer.class);
        return props;
    }

}
