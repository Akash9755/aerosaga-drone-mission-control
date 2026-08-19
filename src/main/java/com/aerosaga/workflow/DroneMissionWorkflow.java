package com.aerosaga.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import io.temporal.workflow.SignalMethod;

@WorkflowInterface
public interface DroneMissionWorkflow {

    @WorkflowMethod
    void executeMission();

    @SignalMethod
    void abortMission();

    @SignalMethod
    void returnHome();
}
