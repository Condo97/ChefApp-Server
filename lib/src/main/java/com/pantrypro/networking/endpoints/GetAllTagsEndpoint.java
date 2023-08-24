package com.pantrypro.networking.endpoints;

import com.pantrypro.core.generation.tagging.TagFetcher;
import com.pantrypro.networking.responsefactories.BodyResponseFactory;
import com.pantrypro.networking.server.response.BodyResponse;
import com.pantrypro.networking.server.response.GetAllTagsResponse;

public class GetAllTagsEndpoint {

    public static BodyResponse getAllTags() {
        var getAllTagsResponse = new GetAllTagsResponse(TagFetcher.getFetchedTagsLowercase());

        return BodyResponseFactory.createSuccessBodyResponse(getAllTagsResponse);
    }

}
