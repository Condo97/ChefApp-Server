package com.pantrypro.networking.endpoints;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pantrypro.core.Endpoint;
import com.pantrypro.core.UserAuthenticator;
import com.pantrypro.database.dao.pooled.ImageSearchCacheDAOPooled;
import com.pantrypro.networking.SerperClient;
import com.pantrypro.networking.server.request.SearchImagesRequest;
import com.pantrypro.networking.server.response.SearchImagesResponse;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class SearchImagesEndpoint implements Endpoint<SearchImagesRequest> {

    private static final long CACHE_TTL_DAYS = 30;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object getResponse(SearchImagesRequest request) throws Exception {
        // Validate auth
        UserAuthenticator.getUserIDFromAuthToken(request.getAuthToken());

        // Normalize query
        String normalizedQuery = request.getQuery().trim().toLowerCase();
        int count = request.getCount() > 0 ? request.getCount() : 10;

        // Hash the query
        String queryHash = sha256(normalizedQuery);

        // Check cache
        List<String> imageUrls = null;
        try {
            String cachedJson = ImageSearchCacheDAOPooled.getCachedImageUrls(queryHash);
            if (cachedJson != null) {
                imageUrls = objectMapper.readValue(cachedJson, new TypeReference<List<String>>() {});
            }
        } catch (Exception e) {
            // Cache read failure - proceed without cache
            System.out.println("Image search cache read failed: " + e.getMessage());
        }

        // Cache miss - call Serper API
        if (imageUrls == null) {
            try {
                imageUrls = SerperClient.searchImages(normalizedQuery, count);
            } catch (Exception e) {
                System.out.println("Serper API call failed: " + e.getMessage());
                e.printStackTrace();
                // Return empty results on Serper failure rather than crashing
                return new SearchImagesResponse(new ArrayList<>());
            }

            // Cache the results (best effort)
            try {
                String imageUrlsJson = objectMapper.writeValueAsString(imageUrls);
                ImageSearchCacheDAOPooled.cacheImageUrls(queryHash, normalizedQuery, imageUrlsJson, CACHE_TTL_DAYS);
            } catch (Exception e) {
                // Cache write failure - not critical, just log
                System.out.println("Image search cache write failed: " + e.getMessage());
            }
        }

        return new SearchImagesResponse(imageUrls);
    }

    /**
     * Computes the SHA-256 hash of the input string and returns it as a hex string.
     */
    private static String sha256(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
