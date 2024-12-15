package com.pantrypro.networking.server.request;

public class GetAndDuplicateRecipeRequest extends AuthRequest {

    private Integer recipeID;

    public GetAndDuplicateRecipeRequest() {

    }

    public GetAndDuplicateRecipeRequest(String authToken, Integer recipeID) {
        super(authToken);
        this.recipeID = recipeID;
    }

    public Integer getRecipeID() {
        return recipeID;
    }

}
