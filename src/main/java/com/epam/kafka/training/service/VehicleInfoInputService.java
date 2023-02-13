package com.epam.kafka.training.service;

import com.epam.kafka.training.model.VehicleInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class VehicleInfoInputService {
    @Value("${kafka.inputTopic}")
    private String inputTopic;
    private final KafkaTemplate<String, VehicleInfo> kafkaTemplate;

    public void send(VehicleInfo vehicleInfo) {
        //TODO: class for additional validation

        log.info("Sending vehicleInfo = '{}'", vehicleInfo);
        kafkaTemplate.send(inputTopic, vehicleInfo.getVehicleId(), vehicleInfo);
        kafkaTemplate.flush();
    }

}
