package com.pantrypro.networking.server.request;

import com.fasterxml.jackson.annotation.JsonValue;

public class TikTokSearchRequest extends AuthRequest {

    public enum Category { // The search category that corresponds to the one in TikAPI
        GENERAL("general"),
        USERS("users"),
        VIDEOS("videos"),
        AUTOCOMPLETE("autocomplete");

        private String id;

        Category(String id) {
            this.id = id;
        }

        @JsonValue // I forget what jsonvalue and jsongetter do :( the difference
        public String getId() {
            return id;
        }
    }

    private Category category;
    private String query;
    private String nextCursor;

    public TikTokSearchRequest() {

    }

    public TikTokSearchRequest(String authToken, Category category, String query, String nextCursor) {
        super(authToken);
        this.category = category;
        this.query = query;
        this.nextCursor = nextCursor;
    }

    public Category getCategory() {
        return category;
    }

    public String getQuery() {
        return query;
    }

    public String getNextCursor() {
        return nextCursor;
    }

}
