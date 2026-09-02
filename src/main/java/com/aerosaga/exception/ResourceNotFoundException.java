package com.aerosaga.exception;

/**
 * Thrown whenever a requested Drone, Mission, or other resource
 * does not exist. Translated into a 404 response by GlobalExceptionHandler.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}