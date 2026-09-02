package com.aerosaga.controller;

import com.aerosaga.entity.Mission;
import com.aerosaga.dto.CreateMissionRequest;
import com.aerosaga.dto.MissionResponse;
import com.aerosaga.service.MissionService;
import com.aerosaga.temporal.TemporalConfig;
import com.aerosaga.workflow.DroneMissionWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;
    private final WorkflowClient workflowClient;

    // Supports pagination and optional status filtering, e.g.
    // GET /api/missions?status=ACTIVE&page=0&size=10
    @GetMapping
    public Page<MissionResponse> getAllMissions(
            @RequestParam(required = false) Mission.MissionStatus status,
            Pageable pageable) {
        return missionService.getMissions(status, pageable).map(MissionResponse::from);
    }

    @GetMapping("/{id}")
    public MissionResponse getMission(@PathVariable Long id) {
        return MissionResponse.from(missionService.getMission(id));
    }

    @GetMapping("/by-drone/{droneId}")
    public List<MissionResponse> getMissionsByDrone(@PathVariable Long droneId) {
        return missionService.getMissionsByDrone(droneId).stream()
                .map(MissionResponse::from)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MissionResponse createMission(@Valid @RequestBody CreateMissionRequest request) {
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

        return MissionResponse.from(missionService.attachWorkflowId(mission.getId(), workflowId));
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
        Mission mission = missionService.validateAbortable(id);
        DroneMissionWorkflow workflow = workflowClient.newWorkflowStub(
                DroneMissionWorkflow.class, mission.getTemporalWorkflowId());
        workflow.abortMission();
    }

    @PatchMapping("/{id}/status")
    public MissionResponse updateStatus(@PathVariable Long id, @RequestParam Mission.MissionStatus status) {
        return MissionResponse.from(missionService.updateStatus(id, status));
    }
}