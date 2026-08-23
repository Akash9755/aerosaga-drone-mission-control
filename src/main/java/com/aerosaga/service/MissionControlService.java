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
    private static final String WORKFLOW_ID = "drone-mission-001";

    private final DroneMissionWorkflow workflow;

    public MissionControlService() {
        WorkflowServiceStubs service =
                WorkflowServiceStubs.newServiceStubs(
                        WorkflowServiceStubsOptions.newBuilder()
                                .setTarget("localhost:7233")
                                .build()
                );

        WorkflowClient client = WorkflowClient.newInstance(service);

        this.workflow =
                client.newWorkflowStub(
                        DroneMissionWorkflow.class,
                        WorkflowOptions.newBuilder()
                                .setTaskQueue(TASK_QUEUE)
                                .setWorkflowId(WORKFLOW_ID)
                                .build()
                );
    }

    public void abortMission() {
        workflow.abortMission();
    }

    public void returnHome() {
        workflow.returnHome();
    }

    public String getMissionState() {
        return workflow.getMissionState();
    }
}
