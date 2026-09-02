package com.aerosaga.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "missions")
@Getter
@Setter
@NoArgsConstructor
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drone_id", nullable = false)
    private Drone drone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MissionStatus status = MissionStatus.PENDING;

    @Column(nullable = false)
    private Double pickupLat;

    @Column(nullable = false)
    private Double pickupLng;

    @Column(nullable = false)
    private Double dropLat;

    @Column(nullable = false)
    private Double dropLng;

    // Set by an Activity when the Temporal workflow starts/finishes this mission
    private String temporalWorkflowId;

    // When true, DeliveryActivityImpl deliberately fails the drop so the
    // compensation logic (auto return-to-base) can be demonstrated on demand.
    @Column(nullable = false)
    private Boolean forceDropFailure = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime completedAt;

    public enum MissionStatus {
        PENDING, ACTIVE, COMPLETED, FAILED, ABORTED
    }
}