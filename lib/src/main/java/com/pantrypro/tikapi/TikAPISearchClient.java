package com.pantrypro.tikapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.pantrypro.Constants;
import com.pantrypro.util.URLBuilder;
import httpson.Httpson;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.function.Consumer;

public class TikAPISearchClient {

    private static final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).connectTimeout(Duration.ofMinutes(Constants.AI_TIMEOUT_MINUTES)).build();

    public enum Category {
        GENERAL("general"),
        USERS("users"),
        VIDEOS("videos"),
        AUTOCOMPLETE("autocomplete");

        private String parameterString;

        Category(String parameterString) {
            this.parameterString = parameterString;
        }

        public String getParameterString() {
            return parameterString;
        }

    }

    public static JsonNode search(Category category, String query, String nextCursor, String apiKey) throws IOException, InterruptedException {
        // Add accessToken to HttpRequest with builder
        Consumer<HttpRequest.Builder> httpRequestBuilder = (r) -> {
            r.setHeader("X-API-KEY", apiKey);
            r.setHeader("accept", "application/json");
        };

        // Create URI
        URLBuilder urlBuilder = new URLBuilder(Constants.TIK_API_SEARCH_URI)
                .addPathParameter(category.getParameterString())
                .addQueryParameter("query", query);
        if (nextCursor != null && !nextCursor.isEmpty())
            urlBuilder.addQueryParameter("nextCursor", nextCursor);

        JsonNode response = Httpson.sendGET(
                httpClient,
                urlBuilder.build(),
                httpRequestBuilder
        );

        return response;
    }

}
