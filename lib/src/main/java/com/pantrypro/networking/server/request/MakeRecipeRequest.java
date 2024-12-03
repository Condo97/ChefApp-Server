package com.pantrypro.networking.server.request;

import com.fasterxml.jackson.annotation.JsonInclude;

public class MakeRecipeRequest extends AuthRequest {

    Integer recipeID;
    String additionalInput;
    Integer ideaID;

    public MakeRecipeRequest() {

    }

    public MakeRecipeRequest(String authToken, Integer recipeID, String additionalInput, Integer ideaID) {
        super(authToken);
        this.recipeID = recipeID;
        this.additionalInput = additionalInput;
        this.ideaID = ideaID;
    }


    public Integer getRecipeID() {
        return recipeID;
    }

    public String getAdditionalInput() {
        return additionalInput;
    }

    // LEGACY //

    public Integer getIdeaID() {
        return ideaID;
    }

}
