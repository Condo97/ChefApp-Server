package com.pantrypro.networking.server.request;

public class MakeRecipeRequest extends AuthRequest {

    Integer recipeID;

    public MakeRecipeRequest() {

    }

    public MakeRecipeRequest(String authToken, Integer recipeID) {
        super(authToken);
        this.recipeID = recipeID;
    }

    public Integer getRecipeID() {
        return recipeID;
    }

}
