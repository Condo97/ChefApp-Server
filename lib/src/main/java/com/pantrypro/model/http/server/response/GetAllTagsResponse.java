package com.pantrypro.model.http.server.response;

import java.util.List;

public class GetAllTagsResponse {

    private List<String> tags;

    public GetAllTagsResponse() {

    }

    public GetAllTagsResponse(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }

}
