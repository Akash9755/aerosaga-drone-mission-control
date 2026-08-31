package com.aerosaga.worker;

import com.aerosaga.activity.DeliveryActivity;
import com.aerosaga.activity.DroneActivity;
import com.aerosaga.activity.MissionStatusActivity;
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

    private final MissionStatusActivity missionStatusActivity =
            Workflow.newActivityStub(
                    MissionStatusActivity.class,
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
    public void executeMission(Long missionId) {

        missionState = "TAKEOFF";

        missionStatusActivity.updateStatus(
                missionId,
                "ACTIVE"
        );

        droneActivity.takeoff(missionId);

        if (abortRequested) {
            abortAndReturnHome(missionId);
            return;
        }

        if (returnHomeRequested) {
            returnHomeAndComplete(missionId);
            return;
        }

        missionState = "NAVIGATING_TO_PICKUP";

        droneActivity.navigateToPickup(missionId);

        System.out.println(
                ">>> AFTER NAVIGATION: abortRequested = " + abortRequested
        );

        if (abortRequested) {
            abortAndReturnHome(missionId);
            return;
        }

        if (returnHomeRequested) {
            returnHomeAndComplete(missionId);
            return;
        }

        try {

            missionState = "DROPPING_PACKAGE";

            deliveryActivity.dropPackage();

        } catch (Exception e) {

            missionState = "RETURNING_HOME";

            droneActivity.returnToBase(missionId);

            missionStatusActivity.updateStatus(
                    missionId,
                    "FAILED"
            );

            throw e;
        }

        missionState = "RETURNING_HOME";

        droneActivity.returnToBase(missionId);

        missionStatusActivity.updateStatus(
                missionId,
                "COMPLETED"
        );

        missionState = "COMPLETED";
    }

    private void abortAndReturnHome(Long missionId) {

        missionState = "RETURNING_HOME";

        droneActivity.returnToBase(missionId);

        missionStatusActivity.updateStatus(
                missionId,
                "ABORTED"
        );

        missionState = "ABORTED";
    }

    private void returnHomeAndComplete(Long missionId) {

        missionState = "RETURNING_HOME";

        droneActivity.returnToBase(missionId);

        missionStatusActivity.updateStatus(
                missionId,
                "COMPLETED"
        );

        missionState = "COMPLETED";
    }

    @Override
    public void abortMission() {
        System.out.println(">>> ABORT SIGNAL RECEIVED");
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