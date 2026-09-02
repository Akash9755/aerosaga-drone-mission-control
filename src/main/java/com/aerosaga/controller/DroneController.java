package com.aerosaga.controller;

import com.aerosaga.dto.DroneResponse;
import com.aerosaga.service.DroneService;
import com.aerosaga.entity.Drone;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/drones")
@RequiredArgsConstructor
public class DroneController {

    private final DroneService droneService;

    @GetMapping
    public Page<DroneResponse> getFleet(
            @RequestParam(required = false) Drone.DroneStatus status,
            Pageable pageable) {
        return droneService.getDrones(status, pageable).map(DroneResponse::from);
    }

    @GetMapping("/{id}")
    public DroneResponse getDrone(@PathVariable Long id) {
        return DroneResponse.from(droneService.getDrone(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DroneResponse registerDrone(@RequestParam String model) {
        return DroneResponse.from(droneService.registerDrone(model));
    }

    @PatchMapping("/{id}/status")
    public DroneResponse updateStatus(@PathVariable Long id, @RequestParam Drone.DroneStatus status) {
        return DroneResponse.from(droneService.updateStatus(id, status));
    }
}