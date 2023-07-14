package com.pantrypro.model.http.server.response;

import java.util.List;

public class TagRecipeIdeaResponse {

    private List<String> tags;

    public TagRecipeIdeaResponse() {

    }

    public TagRecipeIdeaResponse(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }

}
