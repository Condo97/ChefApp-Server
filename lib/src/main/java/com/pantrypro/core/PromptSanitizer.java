package com.pantrypro.core;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Sanitizes user input before inclusion in OpenAI prompts to prevent prompt injection attacks.
 * Strips role markers, instruction override patterns, and common injection delimiters.
 */
public class PromptSanitizer {

    private static final int MAX_INPUT_LENGTH = 2000;

    /**
     * Sanitize a single user input string for safe inclusion in a prompt.
     */
    public static String sanitize(String userInput) {
        if (userInput == null) return "";

        String sanitized = userInput;

        // Remove any "system:", "assistant:", "user:" role markers that could confuse the model
        sanitized = sanitized.replaceAll("(?i)(system|assistant|user)\\s*:", "");

        // Remove instruction-override patterns (e.g., "ignore all previous instructions")
        sanitized = sanitized.replaceAll("(?i)(ignore|forget|disregard)\\s+(all|previous|above|prior)\\s+(instructions|context|rules)", "[removed]");

        // Remove common injection delimiters and special tokens
        sanitized = sanitized.replaceAll("(?i)(```|---+|===+|\\[INST]|\\[/INST]|<\\|im_start\\|>|<\\|im_end\\|>)", "");

        // Trim and enforce length limit
        sanitized = sanitized.trim();
        if (sanitized.length() > MAX_INPUT_LENGTH) {
            sanitized = sanitized.substring(0, MAX_INPUT_LENGTH);
        }

        return sanitized;
    }

    /**
     * Sanitize a list of items (ingredients, modifiers, etc.) and join them with commas.
     */
    public static String sanitizeList(List<String> items) {
        if (items == null || items.isEmpty()) return "";

        return items.stream()
                .map(PromptSanitizer::sanitize)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(", "));
    }
}
