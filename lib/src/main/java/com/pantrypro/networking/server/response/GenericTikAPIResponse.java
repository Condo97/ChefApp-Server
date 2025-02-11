package com.pantrypro.networking.server.response;

import com.fasterxml.jackson.databind.JsonNode;

public class GenericTikAPIResponse {

    private JsonNode apiResponse;

    public GenericTikAPIResponse(JsonNode apiResponse) {
        this.apiResponse = apiResponse;
    }

    public JsonNode getApiResponse() {
        return apiResponse;
    }

}
