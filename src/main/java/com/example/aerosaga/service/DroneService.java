package com.example.aerosaga.service;

import com.example.aerosaga.entity.Drone;
import com.example.aerosaga.exception.ResourceNotFoundException;
import com.example.aerosaga.repository.DroneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DroneService {

    private final DroneRepository droneRepository;

    public List<Drone> getAllDrones() {
        return droneRepository.findAll();
    }

    // Paginated + optionally filtered by status, e.g. GET /api/drones?status=FLYING&page=0&size=10
    public Page<Drone> getDrones(Drone.DroneStatus status, Pageable pageable) {
        if (status != null) {
            return droneRepository.findByStatus(status, pageable);
        }
        return droneRepository.findAll(pageable);
    }

    public Drone getDrone(Long id) {
        return droneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drone not found: " + id));
    }

    public Drone registerDrone(String model) {
        Drone drone = new Drone();
        drone.setModel(model);
        drone.setStatus(Drone.DroneStatus.IDLE);
        return droneRepository.save(drone);
    }

    // Called by a Temporal Activity (e.g. on Takeoff / Return to Base)
    public Drone updateStatus(Long droneId, Drone.DroneStatus status) {
        Drone drone = getDrone(droneId);
        drone.setStatus(status);
        drone.setLastUpdated(LocalDateTime.now());
        return droneRepository.save(drone);
    }

    // Called from TelemetryService whenever a GPS/battery ping arrives
    public Drone updatePosition(Long droneId, Double lat, Double lng, Double batteryLevel) {
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