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

    @Override
    public void executeMission() {
        droneActivity.takeoff();
        droneActivity.navigateToPickup();

        try {
            deliveryActivity.dropPackage();
        } catch (Exception e) {
            droneActivity.returnToBase();
            throw e;
        }

        droneActivity.returnToBase();
    }
}
