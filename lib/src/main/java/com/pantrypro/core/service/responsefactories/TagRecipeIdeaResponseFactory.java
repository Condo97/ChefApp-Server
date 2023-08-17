package com.pantrypro.core.service.responsefactories;

import com.pantrypro.model.database.objects.IdeaRecipeTag;
import com.pantrypro.model.http.server.response.TagRecipeIdeaResponse;

import java.util.ArrayList;
import java.util.List;

public class TagRecipeIdeaResponseFactory {

    public static TagRecipeIdeaResponse from(List<IdeaRecipeTag> tags) {
        List<String> tagStrings = new ArrayList<>();

        tags.forEach(tag -> {
            tagStrings.add(tag.getTagLowercase());
        });

        TagRecipeIdeaResponse response = new TagRecipeIdeaResponse(
                tagStrings
        );

        return response;
    }


}
