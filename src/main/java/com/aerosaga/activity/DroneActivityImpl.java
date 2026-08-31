package com.aerosaga.activity;

public class DroneActivityImpl implements DroneActivity {

    @Override
    public void takeoff() {
        System.out.println("Drone takeoff activity started");

        sleep(5);
    }

    @Override
    public void navigateToPickup() {
        System.out.println("Drone navigating to pickup location");

        sleep(30);
    }

    @Override
    public void returnToBase() {
        System.out.println("Drone returning to base");

        sleep(20);
    }

    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Drone activity interrupted", e);
        }
    }
}
