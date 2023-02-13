package com.epam.kafka.training.consumer;

import com.epam.kafka.training.consumer.model.DataEntry;
import com.epam.kafka.training.consumer.util.DistanceCalculator;
import com.epam.kafka.training.model.VehicleInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackerConsumer {
    private final KafkaTemplate<String, Double> kafkaTemplate;
    @Value("${kafka.outputTopic}")
    private String outputTopic;
    private final Map<String, DataEntry> vehicleDataMap = new HashMap<>();

    @KafkaListener(id = "inputConsumer", topics = "${kafka.inputTopic}", containerFactory = "inputListenerFactory")
    public void listen(ConsumerRecord<String, VehicleInfo> record, Acknowledgment ack) {
        log.info("Received payload='{}' with key='{}'", record.value(), record.key());
        Optional.ofNullable(vehicleDataMap.get(record.key()))
                .ifPresentOrElse(prevData -> calculateDistance(prevData, record), () -> addHistory(record));
        ack.acknowledge();
    }

    private void addHistory(ConsumerRecord<String, VehicleInfo> record) {
        vehicleDataMap.put(record.value().getVehicleId(),
                DataEntry.builder().info(record.value()).build());
    }

    private void calculateDistance(DataEntry previousData,
                                   ConsumerRecord<String, VehicleInfo> record) {
        double fullDistance = previousData.getDistance() +
                DistanceCalculator.calcDistance(
                        previousData.getInfo().getLatitude(),
                        previousData.getInfo().getLongitude(),
                        record.value().getLatitude(),
                        record.value().getLongitude());
        vehicleDataMap.put(record.value().getVehicleId(),
                DataEntry.builder().info(record.value()).distance(fullDistance).build());
        kafkaTemplate.send(outputTopic, record.value().getVehicleId(), fullDistance);

    }

}
