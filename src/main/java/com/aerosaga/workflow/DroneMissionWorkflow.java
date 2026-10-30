package com.aerosaga.workflow;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface DroneMissionWorkflow {

    @WorkflowMethod
    void executeMission(Long missionId);

    @SignalMethod
    void abortMission();

    @SignalMethod
    void returnHome();

    @QueryMethod
    String getMissionState();
}