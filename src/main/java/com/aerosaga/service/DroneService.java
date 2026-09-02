package com.aerosaga.service;

import com.aerosaga.entity.Drone;
import com.aerosaga.repository.DroneRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

import java.util.List;

@Service
public class DroneService {

    private final DroneRepository droneRepository;

    public DroneService(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    public List<Drone> getAllDrones() {
        return droneRepository.findAll();
    }

    public Drone getDrone(Long id) {
        return droneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Drone not found"));
    }

    public Drone createDrone(Drone drone) {
        return droneRepository.save(drone);
    }

    public Drone updateDrone(Long id, Drone updatedDrone) {

        Drone drone = getDrone(id);

        drone.setModel(updatedDrone.getModel());
        drone.setStatus(updatedDrone.getStatus());
        drone.setBatteryLevel(updatedDrone.getBatteryLevel());
        drone.setCurrentLat(updatedDrone.getCurrentLat());
        drone.setCurrentLng(updatedDrone.getCurrentLng());
        drone.setLastUpdated(updatedDrone.getLastUpdated());

        return droneRepository.save(drone);
    }

    public void deleteDrone(Long id) {

        if (!droneRepository.existsById(id)) {
            throw new RuntimeException("Drone not found");
        }

        droneRepository.deleteById(id);
    }

    public Drone updatePosition(
            Long droneId,
            Double lat,
            Double lng,
            Double batteryLevel) {

        Drone drone = getDrone(droneId);

        drone.setCurrentLat(lat);
        drone.setCurrentLng(lng);

        if (batteryLevel != null) {
            drone.setBatteryLevel(batteryLevel);
        }

        drone.setLastUpdated(LocalDateTime.now());

        return droneRepository.save(drone);
    }
}
