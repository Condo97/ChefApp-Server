package com.pantrypro.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;

public class PersistentLogger {

    private static final String LOG_ROOT = "logs";
    private static final int MAX_SESSIONS_TO_KEEP = 30;
    private static final DateTimeFormatter SESSION_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private static final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    // Component constants
    public static final String SERVER = "SERVER";
    public static final String CONSOLE = "CONSOLE";
    public static final String OPENROUTER = "OPENROUTER";
    public static final String DATABASE = "DATABASE";
    public static final String AUTH = "AUTH";
    public static final String APPLE = "APPLE";
    public static final String RECIPE = "RECIPE";
    public static final String RATE_LIMIT = "RATE_LIMIT";
    public static final String SERPER = "SERPER";
    public static final String API = "API";

    private static String sessionFolder;
    private static String requestsFolder;
    private static PrintWriter sessionWriter;
    private static PrintWriter errorsWriter;
    private static final Object writeLock = new Object();
    private static boolean initialized = false;
    private static PrintStream originalOut;
    private static PrintStream originalErr;

    public static synchronized void initialize() {
        if (initialized) return;

        try {
            // Create session folder
            String timestamp = LocalDateTime.now().format(SESSION_FORMAT);
            sessionFolder = LOG_ROOT + "/session_" + timestamp;
            requestsFolder = sessionFolder + "/requests";
            Files.createDirectories(Paths.get(requestsFolder));

            // Open writers
            sessionWriter = new PrintWriter(new BufferedWriter(new FileWriter(sessionFolder + "/session.log", true)), true);
            errorsWriter = new PrintWriter(new BufferedWriter(new FileWriter(sessionFolder + "/errors.log", true)), true);

            // Write session header
            writeSessionHeader();

            // Intercept System.out and System.err
            originalOut = System.out;
            originalErr = System.err;

            System.setOut(new PrintStream(new OutputStream() {
                private final StringBuilder buffer = new StringBuilder();
                @Override
                public void write(int b) {
                    if (b == '\n') {
                        String line = buffer.toString();
                        buffer.setLength(0);
                        if (!line.isEmpty()) {
                            log(CONSOLE, "OUT", line);
                        }
                    } else {
                        buffer.append((char) b);
                    }
                }
            }, true));

            System.setErr(new PrintStream(new OutputStream() {
                private final StringBuilder buffer = new StringBuilder();
                @Override
                public void write(int b) {
                    if (b == '\n') {
                        String line = buffer.toString();
                        buffer.setLength(0);
                        if (!line.isEmpty()) {
                            writeLog(CONSOLE, "ERR", line);
                            writeError(CONSOLE, "ERR", line);
                        }
                    } else {
                        buffer.append((char) b);
                    }
                }
            }, true));

            initialized = true;

            // Log initialization
            info(SERVER, "Session started: " + sessionFolder);
            info(SERVER, "Log format: [TIME] [COMPONENT] [LEVEL] message");

            // Cleanup old sessions
            cleanupOldSessions();

        } catch (IOException e) {
            if (originalOut != null) originalOut.println("FATAL: Failed to initialize PersistentLogger: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void writeSessionHeader() {
        String header = "\u2550".repeat(80) + "\n"
                + "SESSION LOG\n"
                + "\u2550".repeat(80) + "\n"
                + "Started:  " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + "\n"
                + "Folder:   " + sessionFolder + "\n"
                + "Format:   [TIME] [COMPONENT] [LEVEL] message\n"
                + "\u2550".repeat(80) + "\n";
        sessionWriter.println(header);
        sessionWriter.flush();

        String errHeader = "\u2550".repeat(80) + "\n"
                + "ERRORS LOG (subset of session.log)\n"
                + "\u2550".repeat(80) + "\n"
                + "Started:  " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + "\n"
                + "\u2550".repeat(80) + "\n";
        errorsWriter.println(errHeader);
        errorsWriter.flush();
    }

    private static void writeLog(String component, String level, String message) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String line = "[" + timestamp + "] [" + component + "] [" + level + "] " + message;
        synchronized (writeLock) {
            if (sessionWriter != null) {
                sessionWriter.println(line);
                sessionWriter.flush();
            }
            if (originalOut != null) {
                originalOut.println(line);
            }
        }
    }

    private static void writeError(String component, String level, String message) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String line = "[" + timestamp + "] [" + component + "] [" + level + "] " + message;
        synchronized (writeLock) {
            if (errorsWriter != null) {
                errorsWriter.println(line);
                errorsWriter.flush();
            }
        }
    }

    public static void log(String component, String level, String message) {
        if (!initialized) {
            // Fallback before init
            System.out.println("[" + component + "] [" + level + "] " + message);
            return;
        }
        writeLog(component, level, message);
    }

    public static void info(String component, String message) {
        log(component, "INFO", message);
    }

    public static void warn(String component, String message) {
        writeLog(component, "WARN", message);
        writeError(component, "WARN", message);
    }

    public static void error(String component, String message) {
        writeLog(component, "ERROR", message);
        writeError(component, "ERROR", message);
    }

    public static void error(String component, String message, Throwable t) {
        error(component, message);
        if (t != null) {
            StringWriter sw = new StringWriter();
            t.printStackTrace(new PrintWriter(sw));
            writeLog(component, "ERROR", sw.toString());
            writeError(component, "ERROR", sw.toString());
        }
    }

    public static void logJson(String component, String message, Object obj) {
        try {
            String json = objectMapper.writeValueAsString(obj);
            log(component, "INFO", message + "\n" + json);
        } catch (Exception e) {
            log(component, "INFO", message + " [JSON serialization failed: " + e.getMessage() + "]");
        }
    }

    public static String getRequestsFolder() {
        return requestsFolder;
    }

    public static String getSessionFolder() {
        return sessionFolder;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    private static void cleanupOldSessions() {
        try {
            File logDir = new File(LOG_ROOT);
            File[] sessions = logDir.listFiles(f -> f.isDirectory() && f.getName().startsWith("session_"));
            if (sessions != null && sessions.length > MAX_SESSIONS_TO_KEEP) {
                Arrays.sort(sessions, Comparator.comparing(File::getName));
                int toDelete = sessions.length - MAX_SESSIONS_TO_KEEP;
                for (int i = 0; i < toDelete; i++) {
                    info(SERVER, "Cleaning up old session: " + sessions[i].getName());
                    deleteDirectory(sessions[i]);
                }
            }
        } catch (Exception e) {
            warn(SERVER, "Failed to cleanup old sessions: " + e.getMessage());
        }
    }

    private static void deleteDirectory(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) deleteDirectory(f);
                else f.delete();
            }
        }
        dir.delete();
    }

    public static synchronized void shutdown() {
        if (!initialized) return;
        info(SERVER, "Shutting down logger");
        if (sessionWriter != null) { sessionWriter.flush(); sessionWriter.close(); }
        if (errorsWriter != null) { errorsWriter.flush(); errorsWriter.close(); }
        if (originalOut != null) System.setOut(originalOut);
        if (originalErr != null) System.setErr(originalErr);
        initialized = false;
    }
}
