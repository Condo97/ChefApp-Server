package com.pantrypro.database.dao.helpers;

import java.security.SecureRandom;
import java.util.Base64;

public class AuthTokenGenerator {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String generateAuthToken() {
        // Generate AuthToken using cryptographically secure random
        byte[] bytes = new byte[128];
        SECURE_RANDOM.nextBytes(bytes);

        return Base64.getEncoder().encodeToString(bytes);
    }
}
