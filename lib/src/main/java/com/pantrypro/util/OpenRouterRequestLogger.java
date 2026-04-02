package com.pantrypro.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;

public class OpenRouterRequestLogger implements Closeable {

    private static final int MAX_REQUEST_FILES = 500;
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH-mm-ss-SSS");
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private static final DateTimeFormatter FULL_TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    private final String logFilePath;
    private final PrintWriter writer;
    private final LocalDateTime startTime;
    private final Integer userId;
    private final String requestId;

    public OpenRouterRequestLogger(Integer userId) throws IOException {
        this.startTime = LocalDateTime.now();
        this.userId = userId;
        this.requestId = startTime.format(TIME_FORMAT) + "_user" + userId;

        String requestsFolder = PersistentLogger.getRequestsFolder();
        if (requestsFolder == null) {
            throw new IOException("PersistentLogger not initialized — cannot create request log");
        }

        this.logFilePath = requestsFolder + "/openrouter_" + requestId + ".log";
        Files.createDirectories(Paths.get(requestsFolder));
        this.writer = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath)), true);

        writeHeader();
        cleanupOldRequestFiles(requestsFolder);
    }

    private void writeHeader() {
        writer.println("\u2550".repeat(79));
        writer.println("OPENROUTER REQUEST - " + requestId);
        writer.println("\u2550".repeat(79));
        writer.println("User ID:     " + userId);
        writer.println("Start Time:  " + startTime.format(FULL_TIMESTAMP_FORMAT));
        writer.println("\u2550".repeat(79));
        writer.println();
        writer.flush();
    }

    public void logSection(String title, String content) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        writer.println("\u2500\u2500\u2500 " + title + " [" + timestamp + "] \u2500\u2500\u2500");
        writer.println(content);
        writer.println();
        writer.flush();
    }

    public void logRequestDetails(String model, int messageCount, String soClassName) {
        logSection("REQUEST DETAILS",
            "Model:           " + model + "\n" +
            "Message Count:   " + messageCount + "\n" +
            "Schema Class:    " + soClassName);
    }

    public void logOutgoingRequest(Object request) {
        try {
            String json = objectMapper.writeValueAsString(request);
            logSection("OUTGOING REQUEST (to OpenRouter)", json);
        } catch (Exception e) {
            logSection("OUTGOING REQUEST", "[Serialization failed: " + e.getMessage() + "]");
        }
    }

    public void logResponse(String responseContent) {
        try {
            // Try to pretty-print if it's JSON
            Object parsed = objectMapper.readValue(responseContent, Object.class);
            String pretty = objectMapper.writeValueAsString(parsed);
            logSection("RESPONSE", pretty);
        } catch (Exception e) {
            logSection("RESPONSE (raw)", responseContent);
        }
    }

    public void logUsage(int promptTokens, int completionTokens, int totalTokens) {
        logSection("USAGE DETAILS",
            "Prompt Tokens:        " + promptTokens + "\n" +
            "Completion Tokens:    " + completionTokens + "\n" +
            "Total Tokens:         " + totalTokens);
    }

    public void logError(String message, Exception e) {
        StringBuilder sb = new StringBuilder(message);
        if (e != null) {
            sb.append("\n");
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            sb.append(sw);
        }
        logSection("ERROR", sb.toString());
    }

    public void logCompletion(boolean success) {
        long durationMs = java.time.Duration.between(startTime, LocalDateTime.now()).toMillis();
        writer.println("\u2550".repeat(79));
        writer.println("REQUEST " + (success ? "COMPLETED" : "FAILED"));
        writer.println("Duration:     " + durationMs + " ms");
        writer.println("\u2550".repeat(79));
        writer.flush();
    }

    public void log(String message) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        writer.println("[" + timestamp + "] " + message);
        writer.flush();
    }

    @Override
    public void close() {
        if (writer != null) {
            writer.flush();
            writer.close();
        }
    }

    private void cleanupOldRequestFiles(String requestsFolder) {
        try {
            File dir = new File(requestsFolder);
            File[] files = dir.listFiles((d, name) -> name.startsWith("openrouter_") && name.endsWith(".log"));
            if (files != null && files.length > MAX_REQUEST_FILES) {
                Arrays.sort(files, Comparator.comparing(File::getName));
                int toDelete = files.length - MAX_REQUEST_FILES;
                for (int i = 0; i < toDelete; i++) {
                    files[i].delete();
                }
            }
        } catch (Exception e) {
            // Silently ignore cleanup failures
        }
    }

    public String getLogFilePath() {
        return logFilePath;
    }
}
