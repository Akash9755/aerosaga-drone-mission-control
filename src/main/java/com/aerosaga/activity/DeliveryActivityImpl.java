package com.aerosaga.activity;

import com.aerosaga.entity.Mission;
import com.aerosaga.repository.MissionRepository;
import org.springframework.stereotype.Component;

@Component
public class DeliveryActivityImpl implements DeliveryActivity {

    private final MissionRepository missionRepository;

    public DeliveryActivityImpl(MissionRepository missionRepository) {
        this.missionRepository = missionRepository;
    }

    @Override
    public void dropPackage(Long missionId) {

        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Mission not found: " + missionId
                        )
                );

        System.out.println(
                "Package drop activity started for mission "
                        + missionId
        );

        System.out.println(
                "Dropping package at ("
                        + mission.getDropLat()
                        + ", "
                        + mission.getDropLng()
                        + ")"
        );
    }
}