package com.aerosaga.dto;

import com.aerosaga.entity.Mission;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * What the API exposes for a Mission. Flattens the associated Drone down
 * to its id, avoiding lazy-loading surprises and keeping the payload lean.
 */
@Getter
@Builder
public class MissionResponse {

    private final Long id;
    private final Long droneId;
    private final Mission.MissionStatus status;
    private final Double pickupLat;
    private final Double pickupLng;
    private final Double dropLat;
    private final Double dropLng;
    private final String temporalWorkflowId;
    private final Boolean forceDropFailure;
    private final LocalDateTime createdAt;
    private final LocalDateTime completedAt;

    public static MissionResponse from(Mission mission) {
        return MissionResponse.builder()
                .id(mission.getId())
                .droneId(mission.getDrone() != null ? mission.getDrone().getId() : null)
                .status(mission.getStatus())
                .pickupLat(mission.getPickupLat())
                .pickupLng(mission.getPickupLng())
                .dropLat(mission.getDropLat())
                .dropLng(mission.getDropLng())
                .temporalWorkflowId(mission.getTemporalWorkflowId())
//                .forceDropFailure(mission.getForceDropFailure())
                .createdAt(mission.getCreatedAt())
                .completedAt(mission.getCompletedAt())
                .build();
    }
}