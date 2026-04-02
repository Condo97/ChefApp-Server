package com.pantrypro.networking.server.request;

public class SearchImagesRequest extends AuthRequest {

    private String query;
    private int count;

    public SearchImagesRequest() {

    }

    public SearchImagesRequest(String authToken, String query, int count) {
        super(authToken);
        this.query = query;
        this.count = count;
    }

    public String getQuery() {
        return query;
    }

    public int getCount() {
        return count;
    }

}
