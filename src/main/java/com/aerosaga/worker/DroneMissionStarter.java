package com.aerosaga.worker;

import com.aerosaga.workflow.DroneMissionWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;

public class DroneMissionStarter {

    private static final String TASK_QUEUE = "DRONE_MISSION_TASK_QUEUE";

    public static void main(String[] args) {

        WorkflowServiceStubs service =
                WorkflowServiceStubs.newServiceStubs(
                        WorkflowServiceStubsOptions.newBuilder()
                                .setTarget("localhost:7233")
                                .build()
                );

        WorkflowClient client = WorkflowClient.newInstance(service);

        DroneMissionWorkflow workflow =
                client.newWorkflowStub(
                        DroneMissionWorkflow.class,
                        WorkflowOptions.newBuilder()
                                .setTaskQueue(TASK_QUEUE)
                                .setWorkflowId("drone-mission-001")
                                .build()
                );

        workflow.executeMission();

        System.out.println("Drone mission workflow completed.");
    }
}