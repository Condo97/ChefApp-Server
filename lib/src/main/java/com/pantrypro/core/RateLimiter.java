package com.pantrypro.core;

import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * In-memory sliding window rate limiter.
 * Tracks per-user and per-IP request timestamps over a 1-minute window.
 */
public class RateLimiter {

    // Per-user rate limits (by userID)
    private static final ConcurrentHashMap<Integer, Deque<Long>> userRequests = new ConcurrentHashMap<>();
    // Per-user AI rate limits (stricter, for AI-powered endpoints)
    private static final ConcurrentHashMap<Integer, Deque<Long>> userAIRequests = new ConcurrentHashMap<>();
    // Per-IP rate limits
    private static final ConcurrentHashMap<String, Deque<Long>> ipRequests = new ConcurrentHashMap<>();

    // Configurable limits
    private static final int USER_REQUESTS_PER_MINUTE = 30;
    private static final int IP_REQUESTS_PER_MINUTE = 60;
    private static final int AI_REQUESTS_PER_MINUTE = 10;

    private static final long WINDOW_MS = 60_000L; // 1 minute

    /**
     * Check if a user has exceeded the general per-user rate limit.
     * Also records the request if not limited.
     */
    public static boolean isUserRateLimited(int userID) {
        return isRateLimited(userRequests.computeIfAbsent(userID, k -> new ConcurrentLinkedDeque<>()), USER_REQUESTS_PER_MINUTE);
    }

    /**
     * Check if a user has exceeded the stricter AI endpoint rate limit.
     * Also records the request if not limited.
     */
    public static boolean isUserAIRateLimited(int userID) {
        return isRateLimited(userAIRequests.computeIfAbsent(userID, k -> new ConcurrentLinkedDeque<>()), AI_REQUESTS_PER_MINUTE);
    }

    /**
     * Check if an IP has exceeded the per-IP rate limit.
     * Also records the request if not limited.
     */
    public static boolean isIPRateLimited(String ip) {
        if (ip == null || ip.isEmpty()) return false;
        return isRateLimited(ipRequests.computeIfAbsent(ip, k -> new ConcurrentLinkedDeque<>()), IP_REQUESTS_PER_MINUTE);
    }

    /**
     * Sliding window check: remove old entries, check count, record if allowed.
     */
    private static boolean isRateLimited(Deque<Long> timestamps, int maxRequests) {
        long now = System.currentTimeMillis();
        long windowStart = now - WINDOW_MS;

        // Remove expired entries from the front
        while (!timestamps.isEmpty() && timestamps.peekFirst() < windowStart) {
            timestamps.pollFirst();
        }

        // Check if limit exceeded
        if (timestamps.size() >= maxRequests) {
            return true;
        }

        // Record this request
        timestamps.addLast(now);
        return false;
    }

    /**
     * Periodic cleanup of stale entries to prevent memory leaks.
     * Can be called from a scheduled thread or lazily.
     */
    public static void cleanup() {
        long windowStart = System.currentTimeMillis() - WINDOW_MS;

        userRequests.entrySet().removeIf(entry -> {
            Deque<Long> deque = entry.getValue();
            while (!deque.isEmpty() && deque.peekFirst() < windowStart) {
                deque.pollFirst();
            }
            return deque.isEmpty();
        });

        userAIRequests.entrySet().removeIf(entry -> {
            Deque<Long> deque = entry.getValue();
            while (!deque.isEmpty() && deque.peekFirst() < windowStart) {
                deque.pollFirst();
            }
            return deque.isEmpty();
        });

        ipRequests.entrySet().removeIf(entry -> {
            Deque<Long> deque = entry.getValue();
            while (!deque.isEmpty() && deque.peekFirst() < windowStart) {
                deque.pollFirst();
            }
            return deque.isEmpty();
        });
    }

}
