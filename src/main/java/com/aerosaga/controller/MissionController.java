package com.aerosaga.controller;

import com.aerosaga.service.MissionControlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/missions")
public class MissionController {

    private final MissionControlService missionControlService;

    public MissionController(MissionControlService missionControlService) {
        this.missionControlService = missionControlService;
    }

    @PostMapping("/abort")
    public ResponseEntity<String> abortMission() {
        missionControlService.abortMission();
        return ResponseEntity.ok("Mission abort requested");
    }

    @PostMapping("/return-home")
    public ResponseEntity<String> returnHome() {
        missionControlService.returnHome();
        return ResponseEntity.ok("Return home requested");
    }

    @GetMapping("/state")
    public ResponseEntity<String> getMissionState() {
        return ResponseEntity.ok(missionControlService.getMissionState());
    }
}
