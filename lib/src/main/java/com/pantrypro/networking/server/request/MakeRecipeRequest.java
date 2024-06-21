package com.pantrypro.networking.server.request;

import com.fasterxml.jackson.annotation.JsonInclude;

public class MakeRecipeRequest extends AuthRequest {

    Integer recipeID;
    Integer ideaID;

    public MakeRecipeRequest() {

    }

    public MakeRecipeRequest(String authToken, Integer recipeID, Integer ideaID) {
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
