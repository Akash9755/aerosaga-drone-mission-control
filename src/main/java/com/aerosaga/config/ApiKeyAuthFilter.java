package com.aerosaga.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Simple header-based API key check. Every request must include:
 *   X-API-KEY: <configured key>
 * Requests to /actuator/health and the WebSocket handshake are exempt
 * so monitoring tools and the live telemetry connection keep working.
 *
 * This is intentionally lightweight (no JWT/session) since the goal is to
 * demonstrate access control on a student/demo project, not to be a
 * production-grade auth system.
 */
@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    @Value("${aerosaga.security.api-key}")
    private String configuredApiKey;

    private static final List<String> EXEMPT_PATH_PREFIXES = List.of(
            "/actuator/health",
            "/ws/telemetry"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        boolean exempt = EXEMPT_PATH_PREFIXES.stream().anyMatch(path::startsWith);

        if (exempt) {
            filterChain.doFilter(request, response);
            return;
        }

        String providedKey = request.getHeader("X-API-KEY");

        if (providedKey != null && providedKey.equals(configuredApiKey)) {
            var auth = new UsernamePasswordAuthenticationToken("api-client", null, List.of());
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Missing or invalid X-API-KEY header\"}");
        }
    }
}