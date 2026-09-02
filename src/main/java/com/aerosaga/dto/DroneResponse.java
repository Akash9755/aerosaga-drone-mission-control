package com.aerosaga.dto;

import com.aerosaga.entity.Drone;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * What the API actually exposes for a Drone - decoupled from the JPA
 * entity so internal persistence details never leak to clients and the
 * API contract can evolve independently of the database schema.
 */
@Getter
@Builder
public class DroneResponse {

    private final Long id;
    private final String model;
    private final Drone.DroneStatus status;
    private final Double batteryLevel;
    private final Double currentLat;
    private final Double currentLng;
    private final LocalDateTime lastUpdated;

    public static DroneResponse from(Drone drone) {
        return DroneResponse.builder()
                .id(drone.getId())
                .model(drone.getModel())
                .status(drone.getStatus())
                .batteryLevel(drone.getBatteryLevel())
                .currentLat(drone.getCurrentLat())
                .currentLng(drone.getCurrentLng())
                .lastUpdated(drone.getLastUpdated())
                .build();
    }
}