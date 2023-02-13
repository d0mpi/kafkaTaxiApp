package com.epam.kafka.training.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VehicleInfoDto {
    //TODO: id validation
    private String vehicleId;

    @NotNull
    @Min(-90)
    @Max(90)
    private double latitude;

    @NotNull
    @Min(-180)
    @Max(180)
    private double longitude;

}
