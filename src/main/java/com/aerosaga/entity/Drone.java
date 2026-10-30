package com.aerosaga.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "drones")
@Getter
@Setter
@NoArgsConstructor
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String model;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DroneStatus status = DroneStatus.IDLE;

    private Double batteryLevel = 100.0;

    private Double currentLat;

    private Double currentLng;

    private LocalDateTime lastUpdated = LocalDateTime.now();

    public enum DroneStatus {
        IDLE,
        FLYING,
        CHARGING,
        OFFLINE
    }
}