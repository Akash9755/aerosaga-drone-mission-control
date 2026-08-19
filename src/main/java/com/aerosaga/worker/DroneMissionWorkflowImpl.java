package com.aerosaga.worker;

import com.aerosaga.activity.DeliveryActivity;
import com.aerosaga.activity.DroneActivity;
import com.aerosaga.workflow.DroneMissionWorkflow;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

public class DroneMissionWorkflowImpl implements DroneMissionWorkflow {

    private final DroneActivity droneActivity =
            Workflow.newActivityStub(
                    DroneActivity.class,
                    ActivityOptions.newBuilder()
                            .setStartToCloseTimeout(Duration.ofMinutes(1))
                            .setRetryOptions(
                                    RetryOptions.newBuilder()
                                            .setMaximumAttempts(3)
                                            .build()
                            )
                            .build()
            );

    private final DeliveryActivity deliveryActivity =
            Workflow.newActivityStub(
                    DeliveryActivity.class,
                    ActivityOptions.newBuilder()
                            .setStartToCloseTimeout(Duration.ofMinutes(1))
                            .setRetryOptions(
                                    RetryOptions.newBuilder()
                                            .setMaximumAttempts(3)
                                            .build()
                            )
                            .build()
            );

    private boolean abortRequested = false;
    private boolean returnHomeRequested = false;

    @Override
    public void executeMission() {
        droneActivity.takeoff();

        if (abortRequested || returnHomeRequested) {
            droneActivity.returnToBase();
            return;
        }

        droneActivity.navigateToPickup();

        if (abortRequested || returnHomeRequested) {
            droneActivity.returnToBase();
            return;
        }

        try {
            deliveryActivity.dropPackage();
        } catch (Exception e) {
            droneActivity.returnToBase();
            throw e;
        }

        droneActivity.returnToBase();
    }

    @Override
    public void abortMission() {
        abortRequested = true;
    }

    @Override
    public void returnHome() {
        returnHomeRequested = true;
    }
}
