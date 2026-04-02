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
