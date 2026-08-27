package com.aerosaga.service;

import com.aerosaga.workflow.DroneMissionWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import org.springframework.stereotype.Service;

@Service
public class MissionControlService {

    private static final String TASK_QUEUE = "DRONE_MISSION_TASK_QUEUE";

    private final WorkflowClient workflowClient;

    public MissionControlService() {

        WorkflowServiceStubs service =
                WorkflowServiceStubs.newServiceStubs(
                        WorkflowServiceStubsOptions.newBuilder()
                                .setTarget("localhost:7233")
                                .build()
                );

        this.workflowClient = WorkflowClient.newInstance(service);
    }

    public void startMission(Long missionId) {

        String workflowId = "drone-mission-" + missionId;

        DroneMissionWorkflow workflow =
                workflowClient.newWorkflowStub(
                        DroneMissionWorkflow.class,
                        WorkflowOptions.newBuilder()
                                .setTaskQueue(TASK_QUEUE)
                                .setWorkflowId(workflowId)
                                .build()
                );

        WorkflowClient.start(workflow::executeMission, missionId);
    }

    public void abortMission(Long missionId) {

        DroneMissionWorkflow workflow = getWorkflow(missionId);

        workflow.abortMission();
    }

    public void returnHome(Long missionId) {

        DroneMissionWorkflow workflow = getWorkflow(missionId);

        workflow.returnHome();
    }

    public String getMissionState(Long missionId) {

        DroneMissionWorkflow workflow = getWorkflow(missionId);

        return workflow.getMissionState();
    }

    private DroneMissionWorkflow getWorkflow(Long missionId) {

        String workflowId = "drone-mission-" + missionId;

        return workflowClient.newWorkflowStub(
                DroneMissionWorkflow.class,
                workflowId
        );
    }
}