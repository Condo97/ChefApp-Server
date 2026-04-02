package com.pantrypro.networking;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pantrypro.keys.Keys;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SerperClient {

    private static final String SERPER_IMAGES_URL = "https://google.serper.dev/images";
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(30))
            .build();

    /**
     * Searches for images using the Serper API (Google Images).
     *
     * @param query The search query
     * @param count Number of results to request
     * @return List of image URLs
     */
    public static List<String> searchImages(String query, int count) throws IOException, InterruptedException {
        // Build the request body
        String searchQuery = query + " food recipe";
        String requestBody = new ObjectMapper().writeValueAsString(
                new SerperRequestBody(searchQuery, count)
        );

        // Build the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERPER_IMAGES_URL))
                .header("X-API-KEY", Keys.serperApiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Send the request
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Parse the response
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response.body());
        JsonNode imagesNode = rootNode.get("images");

        List<String> imageUrls = new ArrayList<>();
        if (imagesNode != null && imagesNode.isArray()) {
            for (JsonNode imageNode : imagesNode) {
                JsonNode imageUrlNode = imageNode.get("imageUrl");
                if (imageUrlNode != null && !imageUrlNode.isNull()) {
                    imageUrls.add(imageUrlNode.asText());
                }
            }
        }

        return imageUrls;
    }

    /**
     * Simple POJO for the Serper request body, serialized to JSON by Jackson.
     */
    private static class SerperRequestBody {
        private final String q;
        private final int num;

        public SerperRequestBody(String q, int num) {
            this.q = q;
            this.num = num;
        }

        public String getQ() {
            return q;
        }

        public int getNum() {
            return num;
        }
    }

}
