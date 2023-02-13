package com.epam.kafka.training.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoggingConsumer {
    @KafkaListener(id = "outputConsumer", topics = "${kafka.outputTopic}", containerFactory = "outputListenerFactory")
    public void log(String vehicleId, double distance) {
        log.info("Vehicle with id: {} traveled {} km.", vehicleId, distance);
    }
}
