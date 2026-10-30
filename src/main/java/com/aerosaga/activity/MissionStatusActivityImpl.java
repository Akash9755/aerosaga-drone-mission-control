package com.aerosaga.activity;

import com.aerosaga.activity.MissionStatusActivity;
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

        System.out.println(">>> Updating mission " + missionId
                + " status to " + status);

        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() ->
                        new RuntimeException("Mission not found: " + missionId));

        mission.setStatus(
                Mission.MissionStatus.valueOf(status)
        );

        if ("COMPLETED".equals(status)
                || "FAILED".equals(status)
                || "ABORTED".equals(status)) {

            mission.setCompletedAt(
                    java.time.LocalDateTime.now()
            );
        }

        missionRepository.save(mission);

        System.out.println(">>> Mission " + missionId
                + " status updated successfully to " + status);
    }
}

