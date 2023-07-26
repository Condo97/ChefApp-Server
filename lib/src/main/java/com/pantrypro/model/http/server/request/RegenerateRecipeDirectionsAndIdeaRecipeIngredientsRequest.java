package com.pantrypro.model.http.server.request;

import java.util.List;

public class RegenerateRecipeDirectionsAndIdeaRecipeIngredientsRequest extends AuthRequest {

    Integer ideaID;
    String newName;
    String newSummary;
    List<String> measuredIngredients;

    public RegenerateRecipeDirectionsAndIdeaRecipeIngredientsRequest() {

    }

    public RegenerateRecipeDirectionsAndIdeaRecipeIngredientsRequest(String authToken, Integer ideaID, String newName, String newSummary, List<String> measuredIngredients) {
        super(authToken);
        this.ideaID = ideaID;
        this.newName = newName;
        this.newSummary = newSummary;
        this.measuredIngredients = measuredIngredients;
    }

    public Integer getIdeaID() {
        return ideaID;
    }

    public String getNewName() {
        return newName;
    }

    public String getNewSummary() {
        return newSummary;
    }

    public List<String> getMeasuredIngredients() {
        return measuredIngredients;
    }

}
