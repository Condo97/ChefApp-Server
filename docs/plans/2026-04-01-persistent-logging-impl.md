# Persistent Logging System Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Replace broken SLF4J/Log4j2 with WriteSmith-grade persistent file logging — session logs, error logs, and per-request AI call logs.

**Architecture:** Two custom classes: `PersistentLogger` (session-level, intercepts stdout/stderr) and `OpenRouterRequestLogger` (per-AI-call lifecycle). Plus `StartupHealthCheck` for boot diagnostics. All output to `logs/session_YYYY-MM-DD_HH-mm-ss/`.

**Tech Stack:** Pure Java — `PrintWriter`, `java.time`, `ObjectMapper` for JSON pretty-printing. No external logging deps.

---

### Task 1: Create PersistentLogger

**Files:**
- Create: `lib/src/main/java/com/pantrypro/util/PersistentLogger.java`

**Step 1: Write PersistentLogger**

```java
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
```

**Step 2: Verify build**

Run: `./gradlew :lib:compileJava`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add lib/src/main/java/com/pantrypro/util/PersistentLogger.java
git commit -m "Add PersistentLogger — session-level file logging with stdout/stderr interception"
```

---

### Task 2: Create OpenRouterRequestLogger

**Files:**
- Create: `lib/src/main/java/com/pantrypro/util/OpenRouterRequestLogger.java`

**Step 1: Write OpenRouterRequestLogger**

```java
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
```

**Step 2: Verify build**

Run: `./gradlew :lib:compileJava`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add lib/src/main/java/com/pantrypro/util/OpenRouterRequestLogger.java
git commit -m "Add OpenRouterRequestLogger — per-request AI call lifecycle logging"
```

---

### Task 3: Create StartupHealthCheck

**Files:**
- Create: `lib/src/main/java/com/pantrypro/util/StartupHealthCheck.java`

**Step 1: Write StartupHealthCheck**

```java
package com.pantrypro.util;

import com.pantrypro.Constants;
import com.pantrypro.config.EnvConfig;
import com.pantrypro.keys.Keys;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Duration;

public class StartupHealthCheck {

    private static int passed = 0;
    private static int total = 0;

    public static void runAll(String mysqlUrl, String mysqlUser, String mysqlPass) {
        PersistentLogger.info(PersistentLogger.SERVER, "[STARTUP] Running health checks...");
        passed = 0;
        total = 0;

        checkDatabase(mysqlUrl, mysqlUser, mysqlPass);
        checkAppleKeys();
        checkOpenRouterAPI();
        checkSerperAPI();
        checkConfig();

        PersistentLogger.info(PersistentLogger.SERVER, "[HEALTH] " + passed + "/" + total + " checks passed");
    }

    private static void check(String component, String name, Runnable check) {
        total++;
        try {
            check.run();
            passed++;
            PersistentLogger.info(PersistentLogger.SERVER, "[HEALTH-" + component + "] PASS: " + name);
        } catch (Exception e) {
            PersistentLogger.warn(PersistentLogger.SERVER, "[HEALTH-" + component + "] FAIL: " + name + " - " + e.getMessage());
        }
    }

    private static void checkDatabase(String url, String user, String pass) {
        check("DB", "connection", () -> {
            try (Connection conn = DriverManager.getConnection(url, user, pass);
                 Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT 1");
                if (!rs.next()) throw new RuntimeException("No result");
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        });

        check("DB", "recipe_table", () -> {
            try (Connection conn = DriverManager.getConnection(url, user, pass);
                 Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Recipe");
                rs.next();
                int count = rs.getInt(1);
                PersistentLogger.info(PersistentLogger.DATABASE, "  Recipe table: " + count + " rows");
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        });

        check("DB", "user_authtoken_table", () -> {
            try (Connection conn = DriverManager.getConnection(url, user, pass);
                 Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM User_AuthToken");
                rs.next();
                int count = rs.getInt(1);
                PersistentLogger.info(PersistentLogger.DATABASE, "  User_AuthToken table: " + count + " rows");
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }

    private static void checkAppleKeys() {
        check("APPLE", "subscription_key", () -> {
            File f = new File(Constants.Apple_SubscriptionKey_JWS_Path);
            if (!f.exists()) throw new RuntimeException("File not found: " + f.getPath());
            PersistentLogger.info(PersistentLogger.APPLE, "  " + f.getPath() + " (" + f.length() + " bytes)");
        });

        check("APPLE", "apns_key", () -> {
            File f = new File(Constants.Apple_APNS_AuthKey_JWS_Path);
            if (!f.exists()) throw new RuntimeException("File not found: " + f.getPath());
            PersistentLogger.info(PersistentLogger.APPLE, "  " + f.getPath() + " (" + f.length() + " bytes)");
        });
    }

    private static void checkOpenRouterAPI() {
        check("AI", "openrouter_key", () -> {
            String key = Keys.openAiAPI;
            if (key == null || key.equals("REPLACE_ME") || key.isEmpty())
                throw new RuntimeException("Key not set");
            PersistentLogger.info(PersistentLogger.OPENROUTER, "  Key present (length=" + key.length() + ", prefix=" + key.substring(0, Math.min(6, key.length())) + "...)");
        });

        check("AI", "openrouter_api", () -> {
            try {
                HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
                HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create("https://openrouter.ai/api/v1/models"))
                        .timeout(Duration.ofSeconds(10))
                        .GET().build();
                HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
                if (resp.statusCode() != 200) throw new RuntimeException("HTTP " + resp.statusCode());
                PersistentLogger.info(PersistentLogger.OPENROUTER, "  https://openrouter.ai/api/v1/models reachable (HTTP " + resp.statusCode() + ")");
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }

    private static void checkSerperAPI() {
        check("SERPER", "serper_key", () -> {
            String key = Keys.serperApiKey;
            if (key == null || key.equals("REPLACE_ME") || key.isEmpty())
                throw new RuntimeException("Key not set — searchImages endpoint will not work");
        });
    }

    private static void checkConfig() {
        check("CONFIG", "constants", () -> {
            PersistentLogger.info(PersistentLogger.SERVER, "  Port: " + EnvConfig.SERVER_PORT);
            PersistentLogger.info(PersistentLogger.SERVER, "  Free model: " + Constants.DEFAULT_MODEL_NAME);
            PersistentLogger.info(PersistentLogger.SERVER, "  Premium model: " + Constants.PAID_MODEL_NAME);
            PersistentLogger.info(PersistentLogger.SERVER, "  MySQL: " + Constants.MYSQL_URL.replaceAll("password=[^&]*", "password=***"));
            PersistentLogger.info(PersistentLogger.SERVER, "  AI endpoint: " + Constants.OPENAI_URI);
        });
    }
}
```

**Step 2: Verify build**

Run: `./gradlew :lib:compileJava`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add lib/src/main/java/com/pantrypro/util/StartupHealthCheck.java
git commit -m "Add StartupHealthCheck — DB, Apple, AI, config verification at boot"
```

---

### Task 4: Integrate PersistentLogger into Main.java and PantryPro.java

**Files:**
- Modify: `lib/src/main/java/com/pantrypro/Main.java`
- Modify: `lib/src/main/java/com/pantrypro/core/PantryPro.java`

**Step 1: Update Main.java**

At the very start of `main()`, before anything else:
```java
// Initialize persistent logging FIRST
PersistentLogger.initialize();
PersistentLogger.info(PersistentLogger.SERVER, "Starting PantryPro Server — threads=" + MAX_THREADS + ", port=" + EnvConfig.SERVER_PORT);
```

After `SQLConnectionPoolInstance.create(...)`, add:
```java
// Run startup health checks
StartupHealthCheck.runAll(mysqlUrl, mysqlUser, mysqlPass);
```

Replace all `logger.info/warn/error` calls with `PersistentLogger.info/warn/error` using appropriate component constants. Remove the SLF4J `Logger` field and imports.

Add shutdown hook:
```java
Runtime.getRuntime().addShutdownHook(new Thread(() -> PersistentLogger.shutdown()));
```

**Step 2: Wrap doGetStructuredOutput in PantryPro.java**

In `doGetStructuredOutput()`, wrap the AI call with `OpenRouterRequestLogger`:

```java
private static <T> T doGetStructuredOutput(Class<T> soClass, String modelName, List<OAIChatCompletionRequestMessage> messages) throws ... {
    // Create request logger (userId not available here, use 0)
    OpenRouterRequestLogger requestLogger = null;
    try {
        requestLogger = new OpenRouterRequestLogger(0);
    } catch (IOException e) {
        PersistentLogger.warn(PersistentLogger.OPENROUTER, "Failed to create request logger: " + e.getMessage());
    }

    try {
        SOBase soObject = SOJSONSchemaSerializer.objectify(soClass);

        if (requestLogger != null) {
            requestLogger.logRequestDetails(modelName, messages.size(), soClass.getSimpleName());
        }

        OAIChatCompletionRequest chatCompletionRequest = OAIChatCompletionRequest.build(...);

        if (requestLogger != null) {
            requestLogger.logOutgoingRequest(chatCompletionRequest);
        }

        // Log to session log too
        PersistentLogger.info(PersistentLogger.OPENROUTER, "Request started - Model: " + modelName + ", Schema: " + soClass.getSimpleName() +
            (requestLogger != null ? ", Detail log: " + requestLogger.getLogFilePath() : ""));

        OAIGPTChatCompletionResponse response = OAIClient.postChatCompletion(...);

        String responseContent = response.getChoices()[0].getMessage().getContent();
        if (requestLogger != null) {
            requestLogger.logResponse(responseContent);
            requestLogger.logCompletion(true);
        }

        return JSONSchemaDeserializer.deserialize(responseContent, soClass);
    } catch (Exception e) {
        if (requestLogger != null) {
            requestLogger.logError("Request failed: " + e.getMessage(), e);
            requestLogger.logCompletion(false);
        }
        PersistentLogger.error(PersistentLogger.OPENROUTER, "Structured output failed for " + soClass.getSimpleName() + ": " + e.getMessage(), e);
        throw e;
    } finally {
        if (requestLogger != null) requestLogger.close();
    }
}
```

Replace all remaining `logger.*` calls in PantryPro.java with `PersistentLogger.*`.

**Step 3: Verify build**

Run: `./gradlew :lib:compileJava`
Expected: BUILD SUCCESSFUL

**Step 4: Commit**

```bash
git add lib/src/main/java/com/pantrypro/Main.java lib/src/main/java/com/pantrypro/core/PantryPro.java
git commit -m "Integrate PersistentLogger into Main and PantryPro — session logs + per-request AI logs"
```

---

### Task 5: Replace SLF4J in all remaining files

**Files:** All 14 remaining files with SLF4J imports (Server.java + 13 others)

**Step 1: For each file, replace:**
- `import org.slf4j.Logger;` and `import org.slf4j.LoggerFactory;` → `import com.pantrypro.util.PersistentLogger;`
- Remove `private static final Logger logger = LoggerFactory.getLogger(ClassName.class);`
- `logger.info("msg")` → `PersistentLogger.info(PersistentLogger.COMPONENT, "msg")` (choose appropriate component)
- `logger.warn("msg")` → `PersistentLogger.warn(PersistentLogger.COMPONENT, "msg")`
- `logger.error("msg", e)` → `PersistentLogger.error(PersistentLogger.COMPONENT, "msg", e)`
- `logger.error("msg")` → `PersistentLogger.error(PersistentLogger.COMPONENT, "msg")`
- `logger.debug("msg")` → `PersistentLogger.info(PersistentLogger.COMPONENT, "msg")` (no debug level, use info)

**Component mapping:**
- `Server.java` → `SERVER`
- `SQLConnectionPool.java` → `DATABASE`
- `User_AuthTokenDAO.java` → `AUTH`
- `TagFetcher.java`, `TagFilterer.java` → `RECIPE`
- `APNSJWTGenerator.java`, `SendPushNotificationEndpoint.java` → `APPLE`
- `AppleTransactionUpdater.java`, `AppleHttpVerifyReceipt.java`, `RegisterTransactionEndpoint.java` → `APPLE`
- `TransactionDBManager.java` → `DATABASE`
- `PinterestConversionLogger.java` → `API`
- `SpeechTranscriber.java` → `API`
- `GetCreatePanelsResponse.java` → `SERVER`

**Step 2: Verify build**

Run: `./gradlew :lib:compileJava`
Expected: BUILD SUCCESSFUL (0 errors, 0 references to org.slf4j remaining)

**Step 3: Verify no SLF4J references remain**

Run: `grep -r "org.slf4j" lib/src/main/java/`
Expected: No output

**Step 4: Commit**

```bash
git add -A
git commit -m "Replace all SLF4J logger calls with PersistentLogger across 14 files"
```

---

### Task 6: Remove SLF4J/Log4j2 dependencies and config

**Files:**
- Modify: `lib/build.gradle`
- Delete: `lib/src/main/resources/log4j2.xml`

**Step 1: Remove from build.gradle**

Remove these 4 lines:
```groovy
    implementation 'org.apache.logging.log4j:log4j-api:2.20.0'
    implementation 'org.apache.logging.log4j:log4j-core:2.20.0'
    implementation 'org.apache.logging.log4j:log4j-slf4j-impl:2.20.0'
    implementation 'org.slf4j:slf4j-api:2.0.7'
```

**Step 2: Delete log4j2.xml**

```bash
rm lib/src/main/resources/log4j2.xml
```

**Step 3: Verify build**

Run: `./gradlew :lib:build -x test`
Expected: BUILD SUCCESSFUL

**Step 4: Commit**

```bash
git add lib/build.gradle
git rm lib/src/main/resources/log4j2.xml
git commit -m "Remove SLF4J/Log4j2 deps and config — replaced by PersistentLogger"
```

---

### Task 7: Deploy and verify on teenyverse

**Step 1: Deploy new JAR**

```bash
scp lib/out/PantryPro_Server.jar defaultuser@10.0.30.36:/home/defaultuser/App_Servers/PantryPro/PantryPro/PantryPro_Server.jar.new
ssh defaultuser@10.0.30.36 "cd /home/defaultuser/App_Servers/PantryPro/PantryPro && kill $(cat pantrypro.pid) 2>/dev/null; sleep 2; mv PantryPro_Server.jar.new PantryPro_Server.jar && bash start.sh"
```

**Step 2: Wait for startup, check session log**

```bash
ssh defaultuser@10.0.30.36 "sleep 15 && ls -la /home/defaultuser/App_Servers/PantryPro/PantryPro/logs/ | tail -3"
ssh defaultuser@10.0.30.36 "cat /home/defaultuser/App_Servers/PantryPro/PantryPro/logs/session_*/session.log | head -50"
```

Expected: Session header + health check PASS entries + startup info

**Step 3: Test recipe creation and verify request log**

```bash
# Create recipe, then check request log
ssh defaultuser@10.0.30.36 "ls /home/defaultuser/App_Servers/PantryPro/PantryPro/logs/session_*/requests/"
ssh defaultuser@10.0.30.36 "cat /home/defaultuser/App_Servers/PantryPro/PantryPro/logs/session_*/requests/openrouter_*.log | head -80"
```

Expected: Full request lifecycle with header, request details, outgoing JSON, response JSON, usage, completion

**Step 4: Commit final verification**

```bash
git commit --allow-empty -m "Verified: PersistentLogger deployed and working on production"
```
