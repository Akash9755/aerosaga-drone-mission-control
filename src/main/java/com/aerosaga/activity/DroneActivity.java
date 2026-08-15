package com.aerosaga.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface DroneActivity {

    @ActivityMethod
    void takeoff();

    @ActivityMethod
    void navigateToPickup();
}
