package com.pantrypro.core.service.endpoints;

import com.pantrypro.core.generation.tagging.TagFetcher;
import com.pantrypro.core.service.BodyResponseFactory;
import com.pantrypro.model.http.server.request.AuthRequest;
import com.pantrypro.model.http.server.response.BodyResponse;
import com.pantrypro.model.http.server.response.GetAllTagsResponse;

public class GetAllTagsEndpoint {

    public static BodyResponse getAllTags() {
        var getAllTagsResponse = new GetAllTagsResponse(TagFetcher.fetchTagsLowercase());

        return BodyResponseFactory.createSuccessBodyResponse(getAllTagsResponse);
    }

}
