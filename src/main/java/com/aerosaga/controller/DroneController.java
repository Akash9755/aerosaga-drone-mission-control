package com.aerosaga.controller;

import com.aerosaga.entity.Drone;
import com.aerosaga.service.DroneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drones")
public class DroneController {

    private final DroneService droneService;

    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }

    @GetMapping
    public ResponseEntity<List<Drone>> getAllDrones() {
        return ResponseEntity.ok(droneService.getAllDrones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Drone> getDrone(@PathVariable Long id) {
        return ResponseEntity.ok(droneService.getDrone(id));
    }

    @PostMapping
    public ResponseEntity<Drone> createDrone(@RequestBody Drone drone) {
        return ResponseEntity.ok(droneService.createDrone(drone));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Drone> updateDrone(
            @PathVariable Long id,
            @RequestBody Drone drone) {

        return ResponseEntity.ok(
                droneService.updateDrone(id, drone)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDrone(@PathVariable Long id) {
        droneService.deleteDrone(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Drone> updateStatus(
            @PathVariable Long id,
            @RequestParam Drone.DroneStatus status) {

        return ResponseEntity.ok(
                droneService.updateStatus(id, status)
        );
    }
}
