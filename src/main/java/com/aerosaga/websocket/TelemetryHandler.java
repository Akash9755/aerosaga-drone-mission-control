package com.aerosaga.websocket;

import com.aerosaga.service.TelemetryService;
import com.aerosaga.dto.TelemetryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Endpoint: ws://localhost:8080/ws/telemetry
 *
 * Mock drones (or a simulator script) send TelemetryRequest JSON here.
 * The React/Cesium dashboard also connects here to receive live broadcasts.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TelemetryHandler extends TextWebSocketHandler {

    private final TelemetryService telemetryService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        telemetryService.registerSession(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            TelemetryRequest telemetry = objectMapper.readValue(message.getPayload(), TelemetryRequest.class);
            telemetryService.handleTelemetry(telemetry);
        } catch (Exception e) {
            log.error("Bad telemetry payload from session {}", session.getId(), e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        telemetryService.removeSession(session);
    }
}