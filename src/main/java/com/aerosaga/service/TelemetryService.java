package com.aerosaga.service;

import com.aerosaga.entity.Drone;
import com.aerosaga.dto.TelemetryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Handles incoming telemetry pings: persists the drone's latest position,
 * then broadcasts it to every connected dashboard client.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TelemetryService {

    private final DroneService droneService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Every browser session currently watching the fleet dashboard
    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    public void registerSession(WebSocketSession session) {
        sessions.add(session);
        log.info("Telemetry client connected: {} (total: {})", session.getId(), sessions.size());
    }

    public void removeSession(WebSocketSession session) {
        sessions.remove(session);
        log.info("Telemetry client disconnected: {} (total: {})", session.getId(), sessions.size());
    }

    public void handleTelemetry(TelemetryRequest telemetry) {
        Drone updated = droneService.updatePosition(
                telemetry.getDroneId(),
                telemetry.getLat(),
                telemetry.getLng(),
                telemetry.getBatteryLevel()
        );
        broadcast(updated);
    }

    private void broadcast(Drone drone) {
        try {
            String payload = objectMapper.writeValueAsString(drone);
            TextMessage message = new TextMessage(payload);
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(message);
                }
            }
        } catch (IOException e) {
            log.error("Failed to broadcast telemetry", e);
        }
    }
}