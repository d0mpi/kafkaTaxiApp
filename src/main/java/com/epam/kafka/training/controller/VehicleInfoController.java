package com.epam.kafka.training.controller;

import com.epam.kafka.training.model.VehicleInfo;
import com.epam.kafka.training.service.VehicleInfoInputService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/taxi")
@RequiredArgsConstructor
public class VehicleInfoController {
    private final VehicleInfoInputService vehicleInfoInputService;

    @PostMapping("signalize")
    public void updateVehicleInfo(@RequestBody @Valid VehicleInfoDto vehicleInfo) {
        VehicleInfo info = VehicleInfo.builder()
                .longitude(vehicleInfo.getLongitude())
                .latitude(vehicleInfo.getLatitude())
                .vehicleId(vehicleInfo.getVehicleId())
                .build();
        vehicleInfoInputService.send(info);
    }

}

