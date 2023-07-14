package com.pantrypro.core.database.adapters;

import com.pantrypro.model.database.objects.IdeaRecipeTag;
import com.pantrypro.model.http.client.openaigpt.response.functioncall.OAIGPTFunctionCallResponseTagRecipeIdea;

import java.util.ArrayList;
import java.util.List;

public class IdeaRecipeTagFromOpenAIAdapter {

    public static List<IdeaRecipeTag> getIdeaRecipeTags(OAIGPTFunctionCallResponseTagRecipeIdea oaiFunctionCallResponse, Integer ideaID) {
        List<IdeaRecipeTag> tags = new ArrayList<>();

        for (String tag: oaiFunctionCallResponse.getTags()) {
            tags.add(new IdeaRecipeTag(
                    ideaID,
                    tag
            ));
        }

        return tags;
    }

}
