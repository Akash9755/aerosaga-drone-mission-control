package com.example.aerosaga.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Consistent JSON error shape returned for every failed request, e.g.:
 * {
 *   "timestamp": "...",
 *   "status": 404,
 *   "error": "Not Found",
 *   "message": "Drone not found: 42",
 *   "path": "/api/drones/42"
 * }
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String message;
    private final String path;
    private final Map<String, String> fieldErrors;

    public ApiError(int status, String error, String message, String path) {
        this(status, error, message, path, null);
    }

    public ApiError(int status, String error, String message, String path, Map<String, String> fieldErrors) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.fieldErrors = fieldErrors;
    }
}