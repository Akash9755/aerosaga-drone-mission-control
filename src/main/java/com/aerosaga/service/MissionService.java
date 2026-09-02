package com.aerosaga.service;

import com.aerosaga.entity.Drone;
import com.aerosaga.entity.Mission;
import com.aerosaga.repository.MissionRepository;
import com.aerosaga.dto.CreateMissionRequest;
import com.aerosaga.exception.InvalidRequestException;
import com.aerosaga.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final DroneService droneService;

    public List<Mission> getAllMissions() {
        return missionRepository.findAll();
    }

    // Paginated + optionally filtered by status, e.g. GET /api/missions?status=ACTIVE&page=0&size=10
    public Page<Mission> getMissions(Mission.MissionStatus status, Pageable pageable) {
        if (status != null) {
            return missionRepository.findByStatus(status, pageable);
        }
        return missionRepository.findAll(pageable);
    }

    public Mission getMission(Long id) {
        return missionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mission not found: " + id));
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
//        mission.setForceDropFailure(Boolean.TRUE.equals(request.getForceDropFailure()));

        return missionRepository.save(mission);
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

    // Guards against aborting a mission that can no longer be meaningfully aborted
    public Mission validateAbortable(Long missionId) {
        Mission mission = getMission(missionId);
        if (mission.getStatus() == Mission.MissionStatus.COMPLETED
                || mission.getStatus() == Mission.MissionStatus.FAILED) {
            throw new InvalidRequestException(
                    "Mission " + missionId + " is already " + mission.getStatus() + " and cannot be aborted");
        }
        if (mission.getTemporalWorkflowId() == null) {
            throw new InvalidRequestException(
                    "Mission " + missionId + " has no associated workflow yet");
        }
        return mission;
    }
}