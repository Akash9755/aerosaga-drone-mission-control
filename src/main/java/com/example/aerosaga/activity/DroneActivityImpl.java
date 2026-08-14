package com.example.aerosaga.activity;

import com.example.aerosaga.entity.Drone;
import com.example.aerosaga.entity.Mission;
import com.example.aerosaga.service.DroneService;
import com.example.aerosaga.service.MissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * This is where the workflow's abstract steps become real side effects:
 * DB writes via the same services the REST layer uses. In a later phase
 * this could also call real drone hardware/simulator APIs.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DroneActivityImpl implements DroneActivity {

    private final DroneService droneService;
    private final MissionService missionService;

    @Override
    public void takeoff(Long missionId) {
        Mission mission = missionService.getMission(missionId);
        log.info("Mission {}: drone {} taking off", missionId, mission.getDrone().getId());
        droneService.updateStatus(mission.getDrone().getId(), Drone.DroneStatus.FLYING);
        missionService.updateStatus(missionId, Mission.MissionStatus.ACTIVE);
    }

    @Override
    public void navigateToPickup(Long missionId) {
        Mission mission = missionService.getMission(missionId);
        log.info("Mission {}: navigating to pickup point", missionId);
        droneService.updatePosition(
                mission.getDrone().getId(),
                mission.getPickupLat(),
                mission.getPickupLng(),
                null
        );
    }

    @Override
    public void returnToBase(Long missionId) {
        Mission mission = missionService.getMission(missionId);
        log.info("Mission {}: drone {} returning to base", missionId, mission.getDrone().getId());
        droneService.updateStatus(mission.getDrone().getId(), Drone.DroneStatus.IDLE);
    }
}