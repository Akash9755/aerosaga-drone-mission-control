package com.example.aerosaga.service;

import com.example.aerosaga.dto.CreateMissionRequest;
import com.example.aerosaga.entity.Drone;
import com.example.aerosaga.entity.Mission;
import com.example.aerosaga.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final DroneService droneService;

    public List<Mission> getAllMissions() {
        return missionRepository.findAll();
    }

    public Mission getMission(Long id) {
        return missionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Mission not found: " + id));
    }

    public Mission createMission(CreateMissionRequest request) {
        Drone drone = droneService.getDrone(request.getDroneId());

        Mission mission = new Mission();
        mission.setDrone(drone);
        mission.setPickupLat(request.getPickupLat());
        mission.setPickupLng(request.getPickupLng());
        mission.setDropLat(request.getDropLat());
        mission.setDropLng(request.getDropLng());
        mission.setStatus(Mission.MissionStatus.PENDING);

        Mission saved = missionRepository.save(mission);

        // NOTE: this is the handoff point — whoever owns the workflow/
        // package would call WorkflowClient.start(DroneMissionWorkflow::startMission, ...)
        // here (or in the controller) and store the resulting workflow ID
        // via attachWorkflowId() below.

        return saved;
    }

    // Called once the Temporal workflow has been started, to link the two
    public Mission attachWorkflowId(Long missionId, String workflowId) {
        Mission mission = getMission(missionId);
        mission.setTemporalWorkflowId(workflowId);
        return missionRepository.save(mission);
    }

    // Called by a Temporal Activity as the mission progresses
    public Mission updateStatus(Long missionId, Mission.MissionStatus status) {
        Mission mission = getMission(missionId);
        mission.setStatus(status);
        if (status == Mission.MissionStatus.COMPLETED || status == Mission.MissionStatus.FAILED) {
            mission.setCompletedAt(LocalDateTime.now());
        }
        return missionRepository.save(mission);
    }

    public List<Mission> getMissionsByDrone(Long droneId) {
        return missionRepository.findByDroneId(droneId);
    }
}