package com.pantrypro.config;

/**
 * Environment variable configuration with fallback defaults.
 * Allows all hardcoded config to be overridden via environment variables
 * without breaking existing deployments that rely on Constants/Keys defaults.
 */
public class EnvConfig {

    private EnvConfig() {
    }

    public static String get(String key, String defaultValue) {
        String val = System.getenv(key);
        return val != null ? val : defaultValue;
    }

    public static int getInt(String key, int defaultValue) {
        String val = System.getenv(key);
        if (val != null) {
            try {
                return Integer.parseInt(val);
            } catch (NumberFormatException e) {
                System.err.println("EnvConfig: Invalid integer for " + key + "=" + val + ", using default " + defaultValue);
            }
        }
        return defaultValue;
    }

    public static long getLong(String key, long defaultValue) {
        String val = System.getenv(key);
        if (val != null) {
            try {
                return Long.parseLong(val);
            } catch (NumberFormatException e) {
                System.err.println("EnvConfig: Invalid long for " + key + "=" + val + ", using default " + defaultValue);
            }
        }
        return defaultValue;
    }

    public static boolean getBool(String key, boolean defaultValue) {
        String val = System.getenv(key);
        if (val != null) {
            return Boolean.parseBoolean(val);
        }
        return defaultValue;
    }

    // Server config
    public static final int SERVER_PORT = getInt("PANTRYPRO_PORT", 800);
    public static final boolean SSL_ENABLED = getBool("PANTRYPRO_SSL_ENABLED", true);
    public static final String SSL_KEYSTORE = get("PANTRYPRO_SSL_KEYSTORE", "chitchatserver.com.jks");
    public static final String SSL_PASSWORD = get("PANTRYPRO_SSL_PASSWORD", null);

    // Database config
    public static final String MYSQL_URL = get("PANTRYPRO_MYSQL_URL", "jdbc:mysql://localhost:3306/pantrypro_schema?autoReconnect=true");
    public static final String MYSQL_USER = get("PANTRYPRO_MYSQL_USER", null);
    public static final String MYSQL_PASS = get("PANTRYPRO_MYSQL_PASS", null);

    // OpenAI config
    public static final String OPENAI_API_KEY = get("PANTRYPRO_OPENAI_API_KEY", null);
    public static final String DEFAULT_MODEL_NAME = get("PANTRYPRO_DEFAULT_MODEL", "gpt-4o-mini");
    public static final String PAID_MODEL_NAME = get("PANTRYPRO_PAID_MODEL", "gpt-4o");

    // External API keys
    public static final String SERPER_API_KEY = get("PANTRYPRO_SERPER_API_KEY", null);
    public static final String TIKAPI_KEY = get("PANTRYPRO_TIKAPI_KEY", null);
    public static final String PINTEREST_ACCESS_TOKEN = get("PANTRYPRO_PINTEREST_ACCESS_TOKEN", null);
    public static final String PINTEREST_AD_ACCOUNT_ID = get("PANTRYPRO_PINTEREST_AD_ACCOUNT_ID", null);

    // Apple config
    public static final String APPLE_APNS_AUTH_KEY_PATH = get("PANTRYPRO_APNS_AUTH_KEY_PATH", "keys/AuthKey_HZ574FFQUD.p8");
    public static final String APPLE_SUBSCRIPTION_KEY_PATH = get("PANTRYPRO_SUBSCRIPTION_KEY_PATH", "keys/SubscriptionKey_253R52D9UP.p8");

}
