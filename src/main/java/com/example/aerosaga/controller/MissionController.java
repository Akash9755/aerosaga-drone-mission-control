package com.example.aerosaga.controller;

import com.example.aerosaga.dto.CreateMissionRequest;
import com.example.aerosaga.entity.Mission;
import com.example.aerosaga.service.MissionService;
import com.example.aerosaga.temporal.TemporalConfig;
import com.example.aerosaga.workflow.DroneMissionWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;
    private final WorkflowClient workflowClient;

    @GetMapping
    public List<Mission> getAllMissions() {
        return missionService.getAllMissions();
    }

    @GetMapping("/{id}")
    public Mission getMission(@PathVariable Long id) {
        return missionService.getMission(id);
    }

    @GetMapping("/by-drone/{droneId}")
    public List<Mission> getMissionsByDrone(@PathVariable Long droneId) {
        return missionService.getMissionsByDrone(droneId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mission createMission(@Valid @RequestBody CreateMissionRequest request) {
        Mission mission = missionService.createMission(request);

        String workflowId = "mission-" + mission.getId();
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue(TemporalConfig.TASK_QUEUE)
                .setWorkflowId(workflowId)
                .build();

        DroneMissionWorkflow workflow =
                workflowClient.newWorkflowStub(DroneMissionWorkflow.class, options);

        // Async start: fire the workflow and return immediately, don't
        // block the HTTP response on the whole mission finishing.
        WorkflowClient.start(workflow::startMission, mission.getId());

        return missionService.attachWorkflowId(mission.getId(), workflowId);
    }

    @GetMapping("/{id}/status-live")
    public String getLiveWorkflowStatus(@PathVariable Long id) {
        Mission mission = missionService.getMission(id);
        DroneMissionWorkflow workflow = workflowClient.newWorkflowStub(
                DroneMissionWorkflow.class, mission.getTemporalWorkflowId());
        return workflow.getCurrentStep();
    }

    @PostMapping("/{id}/abort")
    public void abortMission(@PathVariable Long id) {
        Mission mission = missionService.getMission(id);
        DroneMissionWorkflow workflow = workflowClient.newWorkflowStub(
                DroneMissionWorkflow.class, mission.getTemporalWorkflowId());
        workflow.abortMission();
    }

    @PatchMapping("/{id}/status")
    public Mission updateStatus(@PathVariable Long id, @RequestParam Mission.MissionStatus status) {
        return missionService.updateStatus(id, status);
    }
}