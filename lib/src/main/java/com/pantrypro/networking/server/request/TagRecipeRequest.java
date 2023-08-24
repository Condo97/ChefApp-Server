package com.pantrypro.networking.server.request;

public class TagRecipeRequest extends AuthRequest {

    Integer recipeID;

    public TagRecipeRequest() {

    }

    public TagRecipeRequest(String authToken, Integer recipeID) {
        super(authToken);
        this.recipeID = recipeID;
    }

    public Integer getRecipeID() {
        return recipeID;
    }

}
