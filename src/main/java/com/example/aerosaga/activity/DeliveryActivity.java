package com.example.aerosaga.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface DeliveryActivity {

    @ActivityMethod
    void dropPackage(Long missionId);
}