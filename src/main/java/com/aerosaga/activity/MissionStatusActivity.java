package com.aerosaga.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface MissionStatusActivity {

    @ActivityMethod
    void updateStatus(Long missionId, String status);

}