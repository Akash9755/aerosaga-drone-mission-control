package com.aerosaga.service;

import com.aerosaga.dto.CreateMissionRequest;
import com.aerosaga.entity.Drone;
import com.aerosaga.entity.Mission;
import com.aerosaga.repository.DroneRepository;
import com.aerosaga.repository.MissionRepository;
import org.springframework.stereotype.Service;

@Service
public class MissionService {

    private final DroneRepository droneRepository;
    private final MissionRepository missionRepository;
    private final MissionControlService missionControlService;

    public MissionService(
            DroneRepository droneRepository,
            MissionRepository missionRepository,
            MissionControlService missionControlService) {

        this.droneRepository = droneRepository;
        this.missionRepository = missionRepository;
        this.missionControlService = missionControlService;
    }

    public Mission createMission(CreateMissionRequest request) {

        Drone drone = droneRepository.findById(request.getDroneId())
                .orElseThrow(() -> new RuntimeException("Drone not found"));

        Mission mission = new Mission();

        mission.setDrone(drone);
        mission.setPickupLat(request.getPickupLat());
        mission.setPickupLng(request.getPickupLng());
        mission.setDropLat(request.getDropLat());
        mission.setDropLng(request.getDropLng());

        Mission savedMission = missionRepository.save(mission);

        missionControlService.startMission(savedMission.getId());

        savedMission.setTemporalWorkflowId(
                "drone-mission-" + savedMission.getId()
        );

        return missionRepository.save(savedMission);
    }
}