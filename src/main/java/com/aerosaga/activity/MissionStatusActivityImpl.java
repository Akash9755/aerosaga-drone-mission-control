package com.aerosaga.activity;

import com.aerosaga.entity.Mission;
import com.aerosaga.repository.MissionRepository;
import org.springframework.stereotype.Component;

@Component
public class MissionStatusActivityImpl implements MissionStatusActivity {

    private final MissionRepository missionRepository;

    public MissionStatusActivityImpl(MissionRepository missionRepository) {
        this.missionRepository = missionRepository;
    }

    @Override
    public void updateStatus(Long missionId, String status) {

        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new RuntimeException("Mission not found"));

        mission.setStatus(
                Mission.MissionStatus.valueOf(status)
        );

        if ("COMPLETED".equals(status) || "FAILED".equals(status)) {
            mission.setCompletedAt(java.time.LocalDateTime.now());
        }

        missionRepository.save(mission);
    }
}