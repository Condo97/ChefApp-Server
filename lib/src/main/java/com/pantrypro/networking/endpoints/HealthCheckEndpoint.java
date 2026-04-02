package com.pantrypro.networking.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pantrypro.connectionpool.SQLConnectionPoolInstance;

import java.sql.Connection;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class HealthCheckEndpoint {

    private static final String VERSION = "1.0.0";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String check() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "ok");
        response.put("version", VERSION);
        response.put("timestamp", DateTimeFormatter.ISO_INSTANT.format(Instant.now()));

        // Check DB connectivity
        String dbStatus = "ok";
        try {
            Connection conn = SQLConnectionPoolInstance.getConnection();
            try {
                if (conn == null || conn.isClosed()) {
                    dbStatus = "error";
                }
            } finally {
                if (conn != null) {
                    SQLConnectionPoolInstance.releaseConnection(conn);
                }
            }
        } catch (Exception e) {
            dbStatus = "error";
        }
        response.put("database", dbStatus);

        try {
            return objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            return "{\"status\":\"ok\",\"version\":\"" + VERSION + "\"}";
        }
    }

}
