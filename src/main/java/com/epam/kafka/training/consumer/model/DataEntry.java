package com.epam.kafka.training.consumer.model;

import com.epam.kafka.training.model.VehicleInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class DataEntry {
    private VehicleInfo info;
    private double distance;
}
