package com.aerosaga.activity;

import com.aerosaga.entity.Drone;
import com.aerosaga.entity.Mission;
import com.aerosaga.repository.DroneRepository;
import com.aerosaga.repository.MissionRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class DroneActivityImpl implements DroneActivity {

    private final DroneRepository droneRepository;
    private final MissionRepository missionRepository;

    public DroneActivityImpl(
            DroneRepository droneRepository,
            MissionRepository missionRepository) {

        this.droneRepository = droneRepository;
        this.missionRepository = missionRepository;
    }

    @Override
    @Transactional
    public void takeoff(Long missionId) {

        System.out.println("Drone takeoff activity started");

        Drone drone = getDroneFromMission(missionId);

        drone.setStatus(Drone.DroneStatus.FLYING);
        drone.setLastUpdated(LocalDateTime.now());

        droneRepository.save(drone);

        sleep(5);
    }

    @Override
    @Transactional
    public void navigateToPickup(Long missionId) {

        System.out.println("Drone navigating to pickup location");

        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() ->
                        new RuntimeException("Mission not found"));

        Drone drone = mission.getDrone();

        drone.setCurrentLat(mission.getPickupLat());
        drone.setCurrentLng(mission.getPickupLng());
        drone.setLastUpdated(LocalDateTime.now());

        droneRepository.save(drone);

        sleep(30);
    }

    @Override
    @Transactional
    public void returnToBase(Long missionId) {

        System.out.println("Drone returning to base");

        Drone drone = getDroneFromMission(missionId);

        drone.setStatus(Drone.DroneStatus.IDLE);
        drone.setLastUpdated(LocalDateTime.now());

        droneRepository.save(drone);

        sleep(20);
    }

    private Drone getDroneFromMission(Long missionId) {

        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() ->
                        new RuntimeException("Mission not found"));

        return mission.getDrone();
    }

    private void sleep(int seconds) {

        try {

            Thread.sleep(seconds * 1000L);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new RuntimeException(
                    "Drone activity interrupted",
                    e
            );
        }
    }
}