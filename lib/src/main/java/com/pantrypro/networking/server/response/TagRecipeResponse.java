package com.pantrypro.networking.server.response;

import java.util.List;

public class TagRecipeResponse {

    private List<String> tags;

    public TagRecipeResponse() {

    }

    public TagRecipeResponse(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }

}
