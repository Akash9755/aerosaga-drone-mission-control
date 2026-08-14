package com.example.aerosaga.workflow;

import com.example.aerosaga.activity.DeliveryActivity;
import com.example.aerosaga.activity.DroneActivity;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

/**
 * Implementation is deterministic Java code — no direct DB/network calls
 * here, only calls to Activities (which do the real work and can be
 * retried/replayed safely by Temporal).
 */
public class DroneMissionWorkflowImpl implements DroneMissionWorkflow {

    private String currentStep = "PENDING";
    private boolean abortRequested = false;

    private final ActivityOptions activityOptions = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofMinutes(5))
            .setRetryOptions(
                    RetryOptions.newBuilder()
                            .setMaximumAttempts(3)
                            .build()
            )
            .build();

    private final DroneActivity droneActivity =
            Workflow.newActivityStub(DroneActivity.class, activityOptions);

    private final DeliveryActivity deliveryActivity =
            Workflow.newActivityStub(DeliveryActivity.class, activityOptions);

    @Override
    public void startMission(Long missionId) {
        try {
            currentStep = "TAKEOFF";
            droneActivity.takeoff(missionId);
            checkAbort();

            currentStep = "NAVIGATE";
            droneActivity.navigateToPickup(missionId);
            checkAbort();

            currentStep = "DROP";
            try {
                deliveryActivity.dropPackage(missionId);
            } catch (Exception dropFailure) {
                // Compensation logic: if the drop fails, don't fail the
                // whole mission silently — send the drone home instead.
                currentStep = "RETURN_AFTER_FAILURE";
                droneActivity.returnToBase(missionId);
                throw dropFailure;
            }
            checkAbort();

            currentStep = "RETURN";
            droneActivity.returnToBase(missionId);

            currentStep = "COMPLETED";
        } catch (AbortException e) {
            currentStep = "ABORTED";
            droneActivity.returnToBase(missionId);
        }
    }

    @Override
    public void abortMission() {
        abortRequested = true;
    }

    @Override
    public String getCurrentStep() {
        return currentStep;
    }

    private void checkAbort() {
        if (abortRequested) {
            throw new AbortException();
        }
    }

    private static class AbortException extends RuntimeException {
    }
}