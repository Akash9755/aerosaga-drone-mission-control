package com.example.aerosaga.controller;

import com.example.aerosaga.entity.Drone;
import com.example.aerosaga.service.DroneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drones")
@RequiredArgsConstructor
public class DroneController {

    private final DroneService droneService;

    @GetMapping
    public List<Drone> getFleet() {
        return droneService.getAllDrones();
    }

    @GetMapping("/{id}")
    public Drone getDrone(@PathVariable Long id) {
        return droneService.getDrone(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Drone registerDrone(@RequestParam String model) {
        return droneService.registerDrone(model);
    }

    @PatchMapping("/{id}/status")
    public Drone updateStatus(@PathVariable Long id, @RequestParam Drone.DroneStatus status) {
        return droneService.updateStatus(id, status);
    }
}