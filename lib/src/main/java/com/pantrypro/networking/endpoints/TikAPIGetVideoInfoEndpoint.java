package com.pantrypro.networking.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.pantrypro.core.Endpoint;
import com.pantrypro.core.UserAuthenticator;
import com.pantrypro.tikapi.TikAPIGetVideoInfoClient;
import com.pantrypro.keys.Keys;
import com.pantrypro.networking.server.request.TikAPIGetVideoInfoRequest;
import com.pantrypro.networking.server.response.GenericTikAPIResponse;

public class TikAPIGetVideoInfoEndpoint implements Endpoint<TikAPIGetVideoInfoRequest> {

    @Override
    public Object getResponse(TikAPIGetVideoInfoRequest request) throws Exception {
        // Get userID
        Integer userID = UserAuthenticator.getUserIDFromAuthToken(request.getAuthToken());

        // Get response from TikAPI
        JsonNode tikAPIGetVideoInfoResponse = TikAPIGetVideoInfoClient.getVideoInfo(
                request.getVideoID(),
                Keys.TikAPI_apiKey
        );

        return new GenericTikAPIResponse(tikAPIGetVideoInfoResponse);
    }

}
