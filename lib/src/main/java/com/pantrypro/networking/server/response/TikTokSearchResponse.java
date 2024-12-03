package com.pantrypro.networking.server.response;

import com.fasterxml.jackson.databind.JsonNode;

public class TikTokSearchResponse {

    private JsonNode apiResponse;

    public TikTokSearchResponse(JsonNode apiResponse) {
        this.apiResponse = apiResponse;
    }

    public JsonNode getApiResponse() {
        return apiResponse;
    }

}
