package com.pantrypro.model.http.server.request;

public class TagRecipeIdeaRequest extends AuthRequest {

    Integer ideaID;

    public TagRecipeIdeaRequest() {

    }

    public TagRecipeIdeaRequest(String authToken, Integer ideaID) {
        super(authToken);
        this.ideaID = ideaID;
    }

    public Integer getIdeaID() {
        return ideaID;
    }

}
