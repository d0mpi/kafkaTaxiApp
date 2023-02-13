package com.epam.kafka.training.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleInfo {
    private String vehicleId;
    private double longitude;
    private double latitude;
}
