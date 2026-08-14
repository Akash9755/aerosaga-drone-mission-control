package com.example.aerosaga.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface DroneActivity {

    @ActivityMethod
    void takeoff(Long missionId);

    @ActivityMethod
    void navigateToPickup(Long missionId);

    @ActivityMethod
    void returnToBase(Long missionId);
}