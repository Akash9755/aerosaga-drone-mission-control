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

    private String missionState = "IDLE";

    @Override
    public void executeMission() {

        missionState = "TAKEOFF";
        droneActivity.takeoff();

        if (abortRequested || returnHomeRequested) {
            missionState = "RETURNING_HOME";
            droneActivity.returnToBase();
            missionState = "COMPLETED";
            return;
        }

        missionState = "NAVIGATING_TO_PICKUP";
        droneActivity.navigateToPickup();

        if (abortRequested || returnHomeRequested) {
            missionState = "RETURNING_HOME";
            droneActivity.returnToBase();
            missionState = "COMPLETED";
            return;
        }

        try {
            missionState = "DROPPING_PACKAGE";
            deliveryActivity.dropPackage();
        } catch (Exception e) {
            missionState = "RETURNING_HOME";
            droneActivity.returnToBase();
            missionState = "FAILED";
            throw e;
        }

        missionState = "RETURNING_HOME";
        droneActivity.returnToBase();

        missionState = "COMPLETED";
    }

    @Override
    public void abortMission() {
        abortRequested = true;
    }

    @Override
    public void returnHome() {
        returnHomeRequested = true;
    }

    @Override
    public String getMissionState() {
        return missionState;
    }
}