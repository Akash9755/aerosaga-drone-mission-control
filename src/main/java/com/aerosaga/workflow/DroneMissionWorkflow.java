package com.aerosaga.workflow;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

/**
 * Orchestrates a single drone delivery mission end to end:
 * Takeoff -> Navigate -> Drop -> Return.
 *
 * This interface is what MissionService uses to start/signal/query
 * the workflow. The actual step-by-step logic lives in the Impl class.
 */
@WorkflowInterface
public interface DroneMissionWorkflow {

    @WorkflowMethod
    void startMission(Long missionId);


    @SignalMethod
    void abortMission();


    @SignalMethod
    void returnHome();


    @QueryMethod
    String getCurrentStep();
}