package com.example.aerosaga.activity;

import com.example.aerosaga.entity.Mission;
import com.example.aerosaga.service.MissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryActivityImpl implements DeliveryActivity {

    private final MissionService missionService;

    @Override
    public void dropPackage(Long missionId) {
        Mission mission = missionService.getMission(missionId);
        log.info("Mission {}: dropping package at ({}, {})",
                missionId, mission.getDropLat(), mission.getDropLng());

        // Simulate a failure condition to exercise the compensation logic
        // (Week 3 "Mid-Project Review" checkpoint) — replace with real
        // hardware/sensor confirmation later.
        boolean dropSucceeded = true;
        if (!dropSucceeded) {
            throw new RuntimeException("Package drop failed for mission " + missionId);
        }

        missionService.updateStatus(missionId, Mission.MissionStatus.COMPLETED);
    }
}