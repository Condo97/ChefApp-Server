package com.pantrypro.networking.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.pantrypro.core.Endpoint;
import com.pantrypro.tikapi.TikAPISearchClient;
import com.pantrypro.database.dao.pooled.User_AuthTokenDAOPooled;
import com.pantrypro.database.objects.User_AuthToken;
import com.pantrypro.keys.Keys;
import com.pantrypro.networking.server.request.TikTokSearchRequest;
import com.pantrypro.networking.server.response.GenericTikAPIResponse;

public class TikTokSearchEndpoint implements Endpoint<TikTokSearchRequest> {

    @Override
    public Object getResponse(TikTokSearchRequest request) throws Exception {
        // Get User_AuthToken to check authToken
        User_AuthToken u_aT = User_AuthTokenDAOPooled.get(request.getAuthToken());

        // Adapt category
        TikAPISearchClient.Category tikAPIClientCategory = switch (request.getCategory()) {
            case USERS -> TikAPISearchClient.Category.USERS;
            case VIDEOS -> TikAPISearchClient.Category.VIDEOS;
            case GENERAL -> TikAPISearchClient.Category.GENERAL;
            case AUTOCOMPLETE -> TikAPISearchClient.Category.AUTOCOMPLETE;
        };

        // Get response from TikAPI
        JsonNode tikAPISearchResponse = TikAPISearchClient.search(
                tikAPIClientCategory,
                request.getQuery(),
                request.getNextCursor(),
                Keys.TikAPI_apiKey
        );

        // Return in TikTokSearchResponse
        return new GenericTikAPIResponse(tikAPISearchResponse);
    }

}
