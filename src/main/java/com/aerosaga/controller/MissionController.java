package com.aerosaga.controller;

import com.aerosaga.dto.CreateMissionRequest;
import com.aerosaga.entity.Mission;
import com.aerosaga.service.MissionControlService;
import com.aerosaga.service.MissionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/missions")
public class MissionController {

    private final MissionService missionService;
    private final MissionControlService missionControlService;

    public MissionController(
            MissionService missionService,
            MissionControlService missionControlService) {

        this.missionService = missionService;
        this.missionControlService = missionControlService;
    }

    @PostMapping
    public ResponseEntity<Mission> createMission(
            @Valid @RequestBody CreateMissionRequest request) {

        Mission mission = missionService.createMission(request);

        return ResponseEntity.ok(mission);
    }

    @PostMapping("/{missionId}/abort")
    public ResponseEntity<String> abortMission(
            @PathVariable Long missionId) {

        missionControlService.abortMission(missionId);

        return ResponseEntity.ok("Mission abort requested");
    }

    @PostMapping("/{missionId}/return-home")
    public ResponseEntity<String> returnHome(
            @PathVariable Long missionId) {

        missionControlService.returnHome(missionId);

        return ResponseEntity.ok("Return home requested");
    }

    @GetMapping("/{missionId}/state")
    public ResponseEntity<String> getMissionState(
            @PathVariable Long missionId) {

        return ResponseEntity.ok(
                missionControlService.getMissionState(missionId)
        );
    }
}