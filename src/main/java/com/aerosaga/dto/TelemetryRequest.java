package com.aerosaga.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Incoming telemetry payload from a (mock) drone, sent over the
 * /ws/telemetry WebSocket connection as JSON and deserialized here.
 */
@Getter
@Setter
public class TelemetryRequest {

    private Long droneId;
    private Double lat;
    private Double lng;
    private Double batteryLevel;
}