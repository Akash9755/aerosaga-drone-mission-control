package com.aerosaga.workflow;

import com.aerosaga.activity.DeliveryActivity;
import com.aerosaga.activity.DroneActivity;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

/**
 * Implementation is deterministic Java code — no direct DB/network calls
 * here, only calls to Activities (which do the real work and can be
 * retried/replayed safely by Temporal).
 *
 * Merged from two branches:
 *  - Base structure, activity retry/timeout config, and the
 *    drop-failure compensation logic are from the original implementation.
 *  - The returnHome() signal and its handling (recall the drone
 *    mid-mission without treating it as a failure) is merged in from
 *    a teammate's branch.
 */
public class DroneMissionWorkflowImpl implements DroneMissionWorkflow {

    private String currentStep = "PENDING";
    private boolean abortRequested = false;
    private boolean returnHomeRequested = false;

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
            checkInterrupts(missionId);

            currentStep = "NAVIGATE";
            droneActivity.navigateToPickup(missionId);
            checkInterrupts(missionId);

            currentStep = "DROP";
            try {
                deliveryActivity.dropPackage(missionId);
            } catch (Exception dropFailure) {

                currentStep = "RETURN_AFTER_FAILURE";
                droneActivity.returnToBase(missionId);
                throw dropFailure;
            }

            currentStep = "RETURN";
            droneActivity.returnToBase(missionId);

            currentStep = "COMPLETED";
        } catch (AbortException e) {
            currentStep = "ABORTED";
            droneActivity.returnToBase(missionId);
        } catch (ReturnHomeException e) {

            currentStep = "RETURN";
            droneActivity.returnToBase(missionId);
            currentStep = "COMPLETED";
        }
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
    public String getCurrentStep() {
        return currentStep;
    }

    /**
     * Checked at each major step boundary. Abort takes priority over a
     * plain return-home request if both were somehow signalled — an
     * emergency abort should never be silently downgraded to a normal
     * recall.
     */
    private void checkInterrupts(Long missionId) {
        if (abortRequested) {
            throw new AbortException();
        }
        if (returnHomeRequested) {
            throw new ReturnHomeException();
        }
    }

    private static class AbortException extends RuntimeException {
    }

    private static class ReturnHomeException extends RuntimeException {
    }
}