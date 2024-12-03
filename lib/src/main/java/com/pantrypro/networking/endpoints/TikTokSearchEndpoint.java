package com.pantrypro.networking.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pantrypro.core.Endpoint;
import com.pantrypro.core.tikapi.TikAPIClient;
import com.pantrypro.database.dao.pooled.User_AuthTokenDAOPooled;
import com.pantrypro.database.objects.User_AuthToken;
import com.pantrypro.keys.Keys;
import com.pantrypro.networking.responsefactories.BodyResponseFactory;
import com.pantrypro.networking.server.request.TikTokSearchRequest;
import com.pantrypro.networking.server.response.TikTokSearchResponse;

public class TikTokSearchEndpoint implements Endpoint<TikTokSearchRequest> {

    @Override
    public Object getResponse(TikTokSearchRequest request) throws Exception {
        // Get User_AuthToken to check authToken
        User_AuthToken u_aT = User_AuthTokenDAOPooled.get(request.getAuthToken());

        // Adapt category
        TikAPIClient.Category tikAPIClientCategory = switch (request.getCategory()) {
            case USERS -> TikAPIClient.Category.USERS;
            case VIDEOS -> TikAPIClient.Category.VIDEOS;
            case GENERAL -> TikAPIClient.Category.GENERAL;
            case AUTOCOMPLETE -> TikAPIClient.Category.AUTOCOMPLETE;
        };

        // Get response from TikAPI
        JsonNode tikAPISearchResponse = TikAPIClient.searchTikAPI(
                tikAPIClientCategory,
                request.getQuery(),
                request.getNextCursor(),
                Keys.TikAPI_apiKey
        );

        // Return in TikTokSearchResponse
        return new TikTokSearchResponse(tikAPISearchResponse);
    }

}
