package com.aerosaga.activity;

public class DroneActivityImpl implements DroneActivity {

    @Override
    public void takeoff() {
        System.out.println("Drone takeoff activity started");
    }

    @Override
    public void navigateToPickup() {
        System.out.println("Drone navigating to pickup location");
    }
}
