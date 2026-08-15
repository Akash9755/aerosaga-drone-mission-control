package com.aerosaga.worker;

import com.aerosaga.activity.DeliveryActivity;
import com.aerosaga.activity.DroneActivity;
import com.aerosaga.workflow.DroneMissionWorkflow;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

public class DroneMissionWorkflowImpl implements DroneMissionWorkflow {

    private final DroneActivity droneActivity =
            Workflow.newActivityStub(
                    DroneActivity.class,
                    ActivityOptions.newBuilder()
                            .setStartToCloseTimeout(Duration.ofMinutes(1))
                            .build()
            );

    private final DeliveryActivity deliveryActivity =
            Workflow.newActivityStub(
                    DeliveryActivity.class,
                    ActivityOptions.newBuilder()
                            .setStartToCloseTimeout(Duration.ofMinutes(1))
                            .build()
            );

    @Override
    public void executeMission() {
        droneActivity.takeoff();
        droneActivity.navigateToPickup();
        deliveryActivity.dropPackage();
    }
}
