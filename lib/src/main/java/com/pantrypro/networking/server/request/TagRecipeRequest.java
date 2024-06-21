package com.pantrypro.networking.server.request;

public class TagRecipeRequest extends AuthRequest {

    Integer recipeID;
    Integer ideaID;

    public TagRecipeRequest() {

    }

    public TagRecipeRequest(String authToken, Integer recipeID, Integer ideaID) {
        super(authToken);
        this.recipeID = recipeID;
        this.ideaID = ideaID;
    }

    public Integer getRecipeID() {
        return recipeID;
    }

    // LEGACY //

    public Integer getIdeaID() {
        return ideaID;
    }

}
