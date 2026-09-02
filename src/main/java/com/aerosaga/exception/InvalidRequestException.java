package com.aerosaga.exception;

/**
 * Thrown for well-formed but semantically invalid requests
 * (e.g. aborting a mission that already completed).
 * Translated into a 400 response by GlobalExceptionHandler.
 */
public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String message) {
        super(message);
    }
}