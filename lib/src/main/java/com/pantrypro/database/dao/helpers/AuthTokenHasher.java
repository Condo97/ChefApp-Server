package com.pantrypro.database.dao.helpers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthTokenHasher {

    /**
     * Hashes a raw auth token using SHA-256 and returns the hex string representation.
     *
     * @param rawToken the raw auth token to hash
     * @return the SHA-256 hex string of the token
     */
    public static String hashToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(rawToken.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder(64);
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 is guaranteed to be available in all JVM implementations
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

}
