package com.example.aerosaga.activity;

import com.example.aerosaga.entity.Mission;
import com.example.aerosaga.service.MissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryActivityImpl implements DeliveryActivity {

    private final MissionService missionService;


    @Value("${aerosaga.mission.random-failure-rate:0.0}")
    private double randomFailureRate;

    @Override
    public void dropPackage(Long missionId) {
        Mission mission = missionService.getMission(missionId);
        log.info("Mission {}: dropping package at ({}, {})",
                missionId, mission.getDropLat(), mission.getDropLng());

        boolean forcedFailure = Boolean.TRUE.equals(mission.getForceDropFailure());
        boolean randomFailure = ThreadLocalRandom.current().nextDouble() < randomFailureRate;
        boolean dropSucceeded = !forcedFailure && !randomFailure;

        if (!dropSucceeded) {
            String reason = forcedFailure ? "forced via forceDropFailure flag" : "random simulated failure";
            log.warn("Mission {}: package drop failed ({})", missionId, reason);
            throw new RuntimeException("Package drop failed for mission " + missionId + " (" + reason + ")");
        }

        missionService.updateStatus(missionId, Mission.MissionStatus.COMPLETED);
    }
}